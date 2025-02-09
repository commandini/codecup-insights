package service;

import com.opencsv.bean.CsvToBeanBuilder;
import model.base.CompetitionRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class CompetitionLoader implements DataLoader<CompetitionRecord> {

    @Override
    public List<CompetitionRecord> load(File resource) throws FileNotFoundException {
        return new CsvToBeanBuilder<CompetitionRecord>(new FileReader(resource))
                .withSeparator(',')
                .withType(CompetitionRecord.class)
                .build()
                .parse();
    }
}
