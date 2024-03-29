package tasks;

public enum TaskStatus {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String url;

    TaskStatus(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
