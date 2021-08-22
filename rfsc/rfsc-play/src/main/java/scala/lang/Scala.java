package scala.lang;

import scala.runtime.AbstractFunction0;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import scala.compat.java8.FutureConverters;


public class Scala extends CrossScala {

    public static <T> T orNull(scala.Option<T> opt) {
        if (opt.isDefined()) {
            return opt.get();
        }
        return null;
    }

    public static <T> T orElse(scala.Option<T> opt, T defaultValue) {
        if (opt.isDefined()) {
            return opt.get();
        }
        return defaultValue;
    }

    public static <K, V> java.util.Map<K, V> asJava(scala.collection.Map<K, V> scalaMap) {
        return scala.collection.JavaConverters.mapAsJavaMapConverter(scalaMap).asJava();
    }

//    public static <K, V> scala.collection.immutable.Map<K, V> asScala(Map<K, V> javaMap) {
//        return play.utils.Conversions.newMap(
//                scala.collection.JavaConverters.mapAsScalaMapConverter(javaMap).asScala().toSeq());
//    }


    public static <A extends B, B> scala.collection.immutable.Seq<B> asScala(
            Collection<A> javaCollection) {
        final scala.collection.immutable.List<A> as =
                scala.collection.JavaConverters.collectionAsScalaIterableConverter(javaCollection)
                        .asScala()
                        .toList();
        @SuppressWarnings("unchecked")
        // covariance: List<A> <: List<B> iff A <: B, given List<A> is covariant in A
        final scala.collection.immutable.List<B> bs = (scala.collection.immutable.List<B>) as;
        return bs;
    }

    public static <A> scala.Function0<A> asScala(final Callable<A> callable) {
        return new AbstractFunction0<A>() {
            @Override
            public A apply() {
                try {
                    return callable.call();
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        };
    }

    public static <A> scala.Function0<scala.concurrent.Future<A>> asScalaWithFuture(
            final Callable<CompletionStage<A>> callable) {
        return new AbstractFunction0<scala.concurrent.Future<A>>() {
            @Override
            public scala.concurrent.Future<A> apply() {
                try {
                    return FutureConverters.toScala(callable.call());
                } catch (RuntimeException | Error e) {
                    throw e;
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        };
    }

    /**
     * Converts a Scala List to Java.
     *
     * @param scalaList the scala list.
     * @return the java list
     * @param <T> the return type.
     */
    public static <T> java.util.List<T> asJava(scala.collection.Seq<T> scalaList) {
        return scala.collection.JavaConverters.seqAsJavaListConverter(scalaList).asJava();
    }

    /**
     * Converts a Scala List to an Array.
     *
     * @param clazz the element class type
     * @param scalaList the scala list.
     * @param <T> the return type.
     * @return the array
     */
    public static <T> T[] asArray(Class<T> clazz, scala.collection.Seq<T> scalaList) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(clazz, scalaList.length());
        scalaList.copyToArray(arr);
        return arr;
    }

    /**
     * Wrap a value into a Scala Option.
     *
     * @param t the java value.
     * @return the converted Option.
     * @param <T> the element type.
     */
    public static <T> scala.Option<T> Option(T t) {
        return scala.Option.apply(t);
    }

    /**
     * @param <T> the type parameter
     * @return a scala {@code None}.
     */
    @SuppressWarnings("unchecked")
    public static <T> scala.Option<T> None() {
        return (scala.Option<T>) scala.None$.MODULE$;
    }

    /**
     * Creates a Scala {@code Tuple2}.
     *
     * @param a element one of the tuple.
     * @param b element two of the tuple.
     * @param <A> input parameter type
     * @param <B> return type.
     * @return an instance of Tuple2 with the elements.
     */
    @SuppressWarnings("unchecked")
    public static <A, B> scala.Tuple2<A, B> Tuple(A a, B b) {
        return new scala.Tuple2<A, B>(a, b);
    }

    /**
     * Converts a scala {@code Tuple2} to a java F.Tuple.
     *
     * @param tuple the Scala Tuple.
     * @param <A> input parameter type
     * @param <B> return type.
     * @return an instance of Tuple with the elements.
     */
    public static <A, B> F.Tuple<A, B> asJava(scala.Tuple2<A, B> tuple) {
        return F.Tuple(tuple._1(), tuple._2());
    }

    /**
     * @param <T> the type parameter
     * @return an empty Scala Seq.
     */
    @SuppressWarnings("unchecked")
    public static <T> scala.collection.Seq<T> emptySeq() {
        return (scala.collection.Seq<T>) toSeq(new Object[] {});
    }

    /**
     * @return an empty Scala Map.
     * @param <A> input parameter type
     * @param <B> return type.
     */
    public static <A, B> scala.collection.immutable.Map<A, B> emptyMap() {
        return new scala.collection.immutable.HashMap<A, B>();
    }

    /**
     * @param <C> the classtag's type.
     * @return an any ClassTag typed according to the Java compiler as C.
     */
    @SuppressWarnings("unchecked")
    public static <C> scala.reflect.ClassTag<C> classTag() {
        return (scala.reflect.ClassTag<C>) scala.reflect.ClassTag$.MODULE$.Any();
    }

//    public static <A, B> scala.PartialFunction<A, B> partialFunction(Function<A, B> f) {
//        return new JavaPartialFunction<A, B>() {
//            @Override
//            public B apply(A a, boolean isCheck) {
//                if (isCheck) return null;
//                else return f.apply(a);
//            }
//        };
//    }
//
//    public static RuntimeException noMatch() {
//        return JavaPartialFunction.noMatch();
//    }
}
