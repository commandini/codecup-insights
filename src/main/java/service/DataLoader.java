package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@FunctionalInterface
public interface DataLoader<T> {
    List<T> load(File resource) throws FileNotFoundException;
}