package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.abc.greendaoexample.db");
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            /* Use forward slashes if you're on macOS or Unix, i.e. "/app/src/main/java"  */
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "\\app\\src\\main\\java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        Entity movie = addMovie(schema);
//        Entity repo = addRepo(schema);

//        Property userId = repo.addLongProperty("userId").notNull().getProperty();
//        user.addToMany(repo, userId, "userRepos");
    }

    private static Entity addMovie(final Schema schema) {
        Entity movie = schema.addEntity("Movie");
        movie.addIdProperty().primaryKey().autoincrement();
        movie.addStringProperty("title").notNull();
        movie.addStringProperty("release_date").notNull();
        movie.addStringProperty("description").notNull();

        return movie;
    }

//    private static Entity addRepo(final Schema schema) {
//        Entity repo = schema.addEntity("Repo");
//        repo.addIdProperty().primaryKey().autoincrement();
//        repo.addStringProperty("title").notNull();
//        repo.addStringProperty("language");
//        repo.addIntProperty("watchers_count");
//
//        return repo;
//    }
}
