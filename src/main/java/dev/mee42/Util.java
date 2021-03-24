package dev.mee42;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Util {

    public static List<String> getSecrets() {
        return makeUnchecked(() -> Files.readAllLines(new File("./key.txt").toPath()));
    }
    public interface Wrapper<T> {
        T execute() throws Exception;
    }
    public interface VoidWrapper { void execute() throws Exception;; }
    public static <T> T makeUnchecked(Wrapper<T> wrapper) {
        try {
            return wrapper.execute();
        } catch (Exception e) {
            throw doThrow(e);
        }
    }
    public static void makeUnchecked(VoidWrapper wrapper) {
        try {
            wrapper.execute();
        } catch (Exception e) {
            throw doThrow(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <E extends Exception> E doThrow(Exception e) throws E {
        throw (E) e;
    }
    public static void requireNonNull(Object...arr) {
        for(Object a : arr) {
            if(a == null) throw new NullPointerException();
        }
    }
    public interface IFolder<A> {
        A fold(A a, A b);
    }
    public static <A> Optional<A> fold(List<A> list, IFolder<A> folder) {
        if(list.size() == 0) return Optional.empty();
        if(list.size() == 1) return Optional.of(list.get(0));
        A a = list.get(0);
        for(int i = 1; i < list.size(); i++){
            a = folder.fold(a, list.get(i));
        }
        return Optional.of(a);
    }
    public static Optional<String> fold(Stream<String> stream) {
        return fold(stream.collect(toList()), (a,b) -> a + "\n" + b);
    }

}
