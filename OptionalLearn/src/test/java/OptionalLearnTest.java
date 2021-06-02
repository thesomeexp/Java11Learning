import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author someexp
 * @date 2021/6/2
 */
public class OptionalLearnTest {

    private static final Logger logger = LoggerFactory.getLogger(OptionalLearnTest.class);

    // region JDK 9 新特性

    /**
     * Java 9 对 Optional 类添加了 or(), ifPresentOrElse(), stream() 三个方法.
     * or() 与 orElse(), orElseGet() 类似, 如果对象是 empty 对象它提供可选的功能.
     * 在这个例子中, 它的返回值是 Supplier 参数提供的其它 Optional 对象.
     * 如果对象包含一个值, 那么 lambda 表达式不会执行
     */
    @Test
    public void whenEmptyOptional_thenGetValueFromOr() {
        User user = null;
        User result = Optional.ofNullable(user)
                .or(() -> Optional.of(new User("default", "1234")))
                .get();

        assertEquals(result.getEmail(), "default");
    }

    /**
     * ifPresentOrElse() 接收两个参数, Consumer 和 Runnable
     * 如果对象有值, 执行 Consumer
     * 否则, 执行 Runnable
     */
    @Test
    public void whenEmptyOptional_thenRunnable() {
        User user = new User("hello@someexp.com", "password");
        Optional.ofNullable(user)
                .ifPresentOrElse(u -> logger.info("User is:" + u.getEmail()),
                        () -> logger.info("User not found"));
    }

    /**
     * stream() 允许你通过将实例转换为 Stream 对象来使用这种对象的优点
     * 如果没有对应值, 可能会返回 empty Stream, 或者返回只包含一个值的 Stream 对象(预防 Optional 包含一个非空的值)
     * 让我们看看如何像处理 Stream 对象一样处理 Optional 对象
     */
    @Test
    public void whenGetStream_thenOk() {
        User user = new User("john@gmail.com", "1234");
        List<String> emails = Optional.ofNullable(user)
                .stream()
                .filter(u -> u.getEmail() != null && u.getEmail().contains("@"))
                .map(u -> u.getEmail())
                .collect(Collectors.toList());

        assertTrue(emails.size() == 1);
        assertEquals(emails.get(0), user.getEmail());
    }
    // endregion
}
