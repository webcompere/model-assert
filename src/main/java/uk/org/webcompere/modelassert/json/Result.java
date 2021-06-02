package uk.org.webcompere.modelassert.json;

public class Result {
    private String expected;
    private String was;
    private boolean isPassed;

    public Result(String expected, String was, boolean isPassed) {
        this.expected = expected;
        this.was = was;
        this.isPassed = isPassed;
    }

    public String getExpected() {
        return expected;
    }

    public Result withExpected(String expected) {
        this.expected = expected;
        return this;
    }

    public String getWas() {
        return was;
    }

    public boolean isPassed() {
        return isPassed;
    }
}
