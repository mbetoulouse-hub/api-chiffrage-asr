package fr.asr.calculator.util;

import fr.asr.calculator.data.dto.enumeration.FrequencyDTO;
import java.util.HashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FrequencyUtil {

    public static <T> void expandRangeKeys(Map<String, T> map) {
        Map<String, T> newMap = new HashMap<>();

        map.entrySet().removeIf(entry -> {
            String[] parts = entry.getKey().split("-");
            if (parts.length == 2) {
                FrequencyDTO first = FrequencyDTO.valueOf(parts[0]);
                FrequencyDTO last = FrequencyDTO.valueOf(parts[1]);

                for (int i = first.ordinal(); i <= last.ordinal(); i++) {
                    newMap.put(FrequencyDTO.values()[i].name(), entry.getValue());
                }

                return true;
            }

            return false;
        });

        map.putAll(newMap);
    }
}
