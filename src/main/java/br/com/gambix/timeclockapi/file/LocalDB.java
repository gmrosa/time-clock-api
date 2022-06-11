package br.com.gambix.timeclockapi.file;

import br.com.gambix.timeclockapi.domain.NoteResult;
import br.com.gambix.timeclockapi.domain.NotesResult;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class LocalDB {

    private final String FOLDER = "/tmp/notes";

    @SneakyThrows
    public void persist(LocalDate date, NoteResult content) {
        content.setDate(date);
        Files.write(Paths.get(getAbsolutePath(date)), new Gson().toJson(content).getBytes());
    }

    @SneakyThrows
    public NotesResult loadMonth(LocalDate date) {
        File folder = new File(getPartialPath(date));

        List<NoteResult> dailyNotes = new ArrayList<>();

        Arrays.asList(folder.listFiles()).forEach(file -> {
            Integer day = Integer.valueOf(file.getName().replaceFirst("[.][^.]+$", ""));
            dailyNotes.add(read(getAbsolutePath(date.getYear(), date.getMonth().name(), day)));
        });
        return NotesResult.builder().dailyNotes(dailyNotes).build();
    }


    public Optional<NoteResult> loadDay(LocalDate date) {
        return Optional.ofNullable(read(getAbsolutePath(date)));
    }

    @SneakyThrows
    public NoteResult read(String absolutePath) {
        File file = new File(absolutePath);

        if (file.exists()) {
            return new Gson().fromJson(Files.readString(Paths.get(absolutePath)), NoteResult.class);
        }
        return null;
    }

    public String getAbsolutePath(LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return getAbsolutePath(date.getYear(), date.getMonth().name(), date.getDayOfMonth());
    }

    @SneakyThrows
    public String getAbsolutePath(Integer year, String month, Integer day) {
        String path = FOLDER + File.separator + year + File.separator + month;
        String filename = day + ".json";
        FileUtils.forceMkdir(new File(path));

        return path + File.separator + filename;
    }

    @SneakyThrows
    public String getPartialPath(LocalDate date) {
        int year = date.getYear();
        String month = date.getMonth().name();
        String path = FOLDER + File.separator + year + File.separator + month;
        File folder = new File(path);
        FileUtils.forceMkdir(folder);

        return path;
    }
}
