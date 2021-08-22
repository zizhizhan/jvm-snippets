package scala.lang;

public class CrossScala {

    public static <T> scala.collection.immutable.Seq<T> toSeq(java.util.List<T> list) {
        return scala.collection.JavaConverters.asScalaBufferConverter(list).asScala().toList();
    }

    public static <T> scala.collection.immutable.Seq<T> toSeq(T[] array) {
        return toSeq(java.util.Arrays.asList(array));
    }

    @SafeVarargs
    public static <T> scala.collection.immutable.Seq<T> varargs(T... array) {
        return toSeq(array);
    }

}
