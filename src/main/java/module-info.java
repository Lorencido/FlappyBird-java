module Flappy.Bird {
    requires java.persistence;
    requires transitive javafx.controls;
    requires javafx.base;
    requires javafx.graphics;
    requires static lombok;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires org.apache.logging.log4j;
    opens org.game to javafx.fxml, org.hibernate.orm.core;
    exports org.game;
}