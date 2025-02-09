package csv.converter;

import com.opencsv.bean.AbstractBeanField;
import model.base.PlayerOrdinal;

public class PlayerOrdinalConverter extends AbstractBeanField<PlayerOrdinal, String> {
    @Override
    protected PlayerOrdinal convert(String value) {
        return PlayerOrdinal.fromValue(value);
    }
}