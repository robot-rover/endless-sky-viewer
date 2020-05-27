module endless.sky.viewer {
    requires logback.classic;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.slf4j;
    requires net.harawata.appdirs;
    requires com.google.gson;
    exports rr.industries;
    exports rr.industries.structures;
}