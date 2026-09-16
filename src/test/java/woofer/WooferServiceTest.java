package woofer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import woofer.exception.WooferException;
import woofer.storage.Storage;

/** Tests command rejection and storage warnings through the shared application service. */
public class WooferServiceTest {
    @TempDir
    private Path directory;

    /**
     * Checks that a rejected task does not change the list or replace the previous undo action.
     *
     * @throws WooferException when valid commands fail.
     */
    @Test
    public void rejectedCommandPreservesTasksAndUndo() throws WooferException {
        WooferService service = new WooferService(new Storage(directory.resolve("tasks.txt")));
        service.execute("  todo   walk dog  ");
        assertThrows(WooferException.class, () -> service.execute("todo broken|record"));
        assertThrows(WooferException.class, () -> service.execute("delete 99"));
        assertTrue(service.execute(" list ").message().contains("walk dog"));
        service.execute("undo");
        assertTrue(service.execute("list").message().contains("list (0)"));
    }

    /**
     * Checks that startup failure prevents later task changes from overwriting the original.
     *
     * @throws IOException when creating the fixture fails.
     * @throws WooferException when valid commands fail.
     */
    @Test
    public void failedLoadDisablesSaving() throws IOException, WooferException {
        Path file = directory.resolve("tasks.txt");
        Files.writeString(file, "corrupted");
        WooferService service = new WooferService(new Storage(file));
        assertTrue(service.hasLoadingError());
        assertTrue(service.getLoadingWarning().contains("saving is disabled"));
        assertTrue(service.execute("todo temporary").warning());
        assertTrue(Files.readString(file).equals("corrupted"));
    }

    /**
     * Checks warning metadata and recovery when the data directory becomes unavailable.
     *
     * @throws IOException when changing the temporary directory fails.
     * @throws WooferException when valid commands fail.
     */
    @Test
    public void failedSaveIsVisibleAndCanRecover() throws IOException, WooferException {
        Path parent = directory.resolve("data");
        WooferService service = new WooferService(new Storage(parent.resolve("tasks.txt")));
        Files.writeString(parent, "blocks directory creation");
        WooferService.Response response = service.execute("todo walk dog");
        assertTrue(response.warning());
        assertTrue(response.message().contains("only in memory"));
        assertFalse(service.execute("list").warning());
        Files.delete(parent);
        assertFalse(service.execute("todo buy food").warning());
        assertTrue(Files.readString(parent.resolve("tasks.txt")).contains("walk dog"));
    }
}
