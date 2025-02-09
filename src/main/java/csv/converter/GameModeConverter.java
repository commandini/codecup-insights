package csv.converter;

import com.opencsv.bean.AbstractBeanField;
import model.base.GameMode;

public class GameModeConverter extends AbstractBeanField<GameMode, String> {
    @Override
    protected GameMode convert(String value) {
        return GameMode.valueOf(value.toUpperCase());
    }
}