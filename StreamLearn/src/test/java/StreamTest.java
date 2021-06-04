import org.junit.Test;

import java.util.stream.Stream;

/**
 * 参考: https://stackify.com/streams-guide-java-8/
 *
 * @author someexp
 * @date 2021/6/4
 */
public class StreamTest {

    // region Java 9 中 Stream 的改进

    /**
     * Java 8 给世界带来了 Java 流. 然而接下来的版本也对该功能做出了贡献. 所以我们简单看一下 Java 9 对 Stream API 带来了啥改进.
     */
    // endregion

    // region takeWhile

    /**
     * takeWhile 方法是一个新增的 Stream API
     * 它的工作和名字暗示的一样: 它会从流中拿 (take) 一个元素, 当 (while) 给定 condition 是 true 时.
     * 当 condition 是 false 时, 它结束然后返回一个新的流, 这个流中的对象就是之前那些匹配 predicate 的.
     * 换句话说, 它有点像有条件的 filter.
     * 在下面的例子中, 我们获得了一个无限流, 然后使用 takeWhile 方法来选择那些小于或等于 10 的数. 然后我们计算它的平方然后打印它们.
     */
    @Test
    public void takeWhileTest() {
        Stream.iterate(1, i -> i + 1)
                .takeWhile(n -> n <= 10)
                .map(x -> x * x)
                .forEach(System.out::println);
    }

    /**
     * 你可能会疑惑于, 这和 filter 有什么区别?
     * 你可以使用下面的代码达到同样的目的:
     */
    @Test
    public void filterTest() {
        Stream.iterate(1, i -> i + 1)
                .filter(x -> x <= 10)
                .map(x -> x * x)
                .forEach(System.out::println);
    }

    /**
     * 好吧, 在上面的特殊情况下, 这两种方法取得了相同的结果, 但这并不意味着总是这样的. 下面就用另一个例子来说明这种差别.
     * 所以区别是 filter 会遍历整个序列, 而 takeWhile 在遇到第一个 false 时就停止了
     */
    @Test
    public void differenceBetweenTakeWhileAndFilter() {
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
                .takeWhile(x -> x <= 5)
                .forEach(System.out::println);
        System.out.println("----------");
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
                .filter(x -> x <= 5)
                .forEach(System.out::println);
    }
    // endregion

    // region dropWhile

    /**
     * dropWhile 方法做的事情和 takeWhile 差不多但相反.
     * 也就是当遇到 true 时, 它丢弃元素, 一旦遇到 false, 就停止并返回.
     */
    @Test
    public void dropWhileTest() {
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)
                .dropWhile(x -> x <= 5)
                .forEach(System.out::println);
    }
    // endregion

    // region iterate

    /**
     * 我们之前已经在 8 代中提到过这个方法. 9 代中它重写了这个方法, 看看有啥区别.
     * 在 8 代中, 这个方法接收两个参数, 一个初始化种子, 还有一个产生下一个值的函数. 它存在的问题是它没有包括一个让循环退出的方法.
     * 当创建无限流时这很好, 但这不总是这样.
     * 在 Java 9 中我们有了新版本的 iterate(), 它新增了一个 predicate 参数来决定什么时候循环应该结束. 只要条件是 true, 就会继续执行.
     */
    @Test
    public void iterateTest() {
        Stream.
                iterate(1, i -> i < 256, i -> i * 2)
                .forEach(System.out::println);
    }
    // endregion

    // region ofNullable

    /**
     * 最后一个 Stream API 新增的项, 它不仅避免了可怕的 NPE, 还使得写出的代码更简洁.
     * 看看下面的例子:
     * 假设 number 引用的 Integer 来自 UI, 网络, 文件系统或者其它外部不可信的源. 它可能为 null.
     * 我们不想为一个 null 元素创建流. 这可能会导致 NPE. 为了避免这种情况, 我们可检查是否为 null 然后返回一个 empty 流.
     */
    @Test
    public void ofNullableTest() {
        Integer number = null;
        Stream<Integer> result = number != null
                ? Stream.of(number)
                : Stream.empty();
    }

    /**
     * 上面的例子是一个被设计好的例子, 在现实生活中, 类似情况下的代码会变得非常混乱. 我们可以使用 ofNullable() 来代替.
     * 下面的方法在收到 null 时返回空的 Optional, 避免了在通常会导致错误的情况下出现运行的错误, 比如在下下面的例子中.
     */
    @Test
    public void ofNullableTest2() {
        Integer number = null;
        Stream<Integer> result = Stream.ofNullable(number);
    }

    @Test
    public void ofNullableTest3() {
        Integer number = null;
        Stream<Integer> result = Stream.ofNullable(number);
        result.map(x -> x * x).forEach(System.out::println);
    }
    // endregion
}
