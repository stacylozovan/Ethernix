package entity;

import nl.saxion.app.CsvReader;
import java.util.*;

public class DialogueLoader {
    public static Map<String, String[]> loadDialogues(CsvReader csvReader) {
        Map<String, List<String>> dialoguesMap = new HashMap<>();

        try {
            csvReader.setSeparator(',');
            csvReader.skipRow();

            while (csvReader.loadRow()) {
                String name = csvReader.getString(0).trim();
                String dialogue = csvReader.getString(1).trim();

                dialoguesMap.putIfAbsent(name, new ArrayList<>());
                dialoguesMap.get(name).add(dialogue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String[]> dialoguesArrayMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : dialoguesMap.entrySet()) {
            dialoguesArrayMap.put(entry.getKey(), entry.getValue().toArray(new String[0]));
        }

        return dialoguesArrayMap;
    }
}
