package com.example.trainingplanner.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trainingplanner.model.entities.ActiveDay;
import com.example.trainingplanner.model.entities.ActiveExercise;
import com.example.trainingplanner.model.entities.CyclePhase;
import com.example.trainingplanner.model.entities.DayTemplate;
import com.example.trainingplanner.model.entities.Exercise;
import com.example.trainingplanner.model.entities.ProgramTemplate;

import java.util.ArrayList;
import java.util.List;

public class AppDatabase extends SQLiteOpenHelper {

    public AppDatabase(Context context) {
        super(context, "training_planner.db", null, 17);
}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE exercises (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, base_weight REAL NOT NULL)");

        db.execSQL("CREATE TABLE day_templates (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL UNIQUE)");

        db.execSQL("CREATE TABLE day_exercises (day_id INTEGER, exercise_id INTEGER, FOREIGN KEY(day_id) REFERENCES day_templates(id) ON DELETE CASCADE, FOREIGN KEY(exercise_id) REFERENCES exercises(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE program_templates (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL UNIQUE)");

        db.execSQL("CREATE TABLE program_days (program_id INTEGER, day_id INTEGER, sequence_order INTEGER, FOREIGN KEY(program_id) REFERENCES program_templates(id) ON DELETE CASCADE, FOREIGN KEY(day_id) REFERENCES day_templates(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE active_cycle (id INTEGER PRIMARY KEY AUTOINCREMENT, program_title TEXT NOT NULL)");

        db.execSQL("CREATE TABLE active_days (id INTEGER PRIMARY KEY AUTOINCREMENT, cycle_id INTEGER, week_number INTEGER, title TEXT, is_completed INTEGER DEFAULT 0, FOREIGN KEY(cycle_id) REFERENCES active_cycle(id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE active_exercises (id INTEGER PRIMARY KEY AUTOINCREMENT, day_id INTEGER, exercise_name TEXT, plan_weight REAL, plan_reps INTEGER, actual_weight REAL DEFAULT 0, actual_reps INTEGER DEFAULT 0, FOREIGN KEY(day_id) REFERENCES active_days(id) ON DELETE CASCADE)");

        fillTestData(db);
    }

    //заполнение тестовыми данными
    private void fillTestData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO program_templates (id, title) VALUES (1, 'Сплит с упором на мышцы дельт и рук')");

        db.execSQL("INSERT INTO day_templates (id, title) VALUES (1, 'День спины')");
        db.execSQL("INSERT INTO day_templates (id, title) VALUES (2, 'День груди')");
        db.execSQL("INSERT INTO day_templates (id, title) VALUES (3, 'День ног')");

        db.execSQL("INSERT INTO program_days (program_id, day_id, sequence_order) VALUES (1, 1, 1)");
        db.execSQL("INSERT INTO program_days (program_id, day_id, sequence_order) VALUES (1, 2, 2)");
        db.execSQL("INSERT INTO program_days (program_id, day_id, sequence_order) VALUES (1, 3, 3)");

        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (1, 'Подъем гантелей в стороны на среднюю дельту', 10.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (2, 'Тяга верхнего блока вертикальным хватом', 41.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (3, 'Тяга в тренажере на спину горизонтальным хватом', 75.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (4, 'Тяга в хаммере на одну руку', 12.5)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (5, 'Задняя дельта в хаммере', 10.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (6, 'Сгибание рук с W-грифом на бицепс', 20.0)");

        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (7, 'Жим штанги лежа', 55.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (8, 'Жим гантелей на диагональной скамье', 14.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (9, 'Французский жим', 20.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (10, 'Задняя дельта в бабочке', 10.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (11, 'Разгибание на трицепс в блоке', 32.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (12, 'Сгибание гантелей на бицепс сидя', 6.0)");

        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (13, 'Разведение гантелей в стороны', 8.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (14, 'Тяга штанги к подбородку (протяжка)', 25.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (15, 'Жим ногами', 200.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (16, 'Разгибание ног в тренажере', 61.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (17, 'Сгибание ног в тренажере', 41.0)");

        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (18, 'Становая тяга', 100.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (19, 'Приседания со штангой', 80.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (20, 'Отжимания на брусьях', 0.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (21, 'Подтягивания на турнике', 0.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (22, 'Шраги с гантелями', 24.0)");
        db.execSQL("INSERT INTO exercises (id, name, base_weight) VALUES (23, 'Гиперэкстензия', 15.0)");

        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (1, 1)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (1, 2)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (1, 3)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (1, 4)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (1, 5)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (1, 6)");

        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (2, 7)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (2, 8)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (2, 9)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (2, 10)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (2, 11)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (2, 12)");

        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (3, 13)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (3, 14)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (3, 15)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (3, 16)");
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (3, 17)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS active_exercises");
        db.execSQL("DROP TABLE IF EXISTS active_days");
        db.execSQL("DROP TABLE IF EXISTS active_cycle");
        db.execSQL("DROP TABLE IF EXISTS program_days");
        db.execSQL("DROP TABLE IF EXISTS program_templates");
        db.execSQL("DROP TABLE IF EXISTS day_exercises");
        db.execSQL("DROP TABLE IF EXISTS day_templates");
        db.execSQL("DROP TABLE IF EXISTS exercises");
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // УПРАЖНЕНИЯ
    public void addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO exercises (name, base_weight) VALUES (?, ?)",
                new Object[]{exercise.getName(), exercise.getBaseWeight()});
        db.close();
    }

    public void deleteExercise(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM exercises WHERE id = ?", new Object[]{id});
        db.close();
    }

    public void updateExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE exercises SET name = ?, base_weight = ? WHERE id = ?",
                new Object[]{exercise.getName(), exercise.getBaseWeight(), exercise.getId()});
        db.close();
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, base_weight FROM exercises", null);

        if (cursor.moveToFirst()) {
            do {
                list.add(new Exercise(cursor.getLong(0), cursor.getString(1), cursor.getDouble(2)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }


    // ШАБЛОНЫ ДНЕЙ
    public long addDayTemplate(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO day_templates (title) VALUES (?)", new Object[]{title});

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        long id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getLong(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    public void addExerciseToDay(long dayId, long exerciseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (?, ?)",
                new Object[]{dayId, exerciseId});
        db.close();
    }

    public void deleteDayTemplate(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM day_templates WHERE id = ?", new Object[]{id});
        db.close();
    }

    public List<Long> getExerciseIdsForDay(long dayId) {
        List<Long> ids = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT exercise_id FROM day_exercises WHERE day_id = ?", new String[]{String.valueOf(dayId)});
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getLong(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ids;
    }

    public void updateDayExercises(long dayId, List<Long> newExerciseIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM day_exercises WHERE day_id = ?", new Object[]{dayId});
        for (Long exId : newExerciseIds) {
            db.execSQL("INSERT INTO day_exercises (day_id, exercise_id) VALUES (?, ?)", new Object[]{dayId, exId});
        }
        db.close();
    }

    public List<DayTemplate> getAllDayTemplatesWithExercises() {
        List<DayTemplate> dayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor dayCursor = db.rawQuery("SELECT id, title FROM day_templates", null);

        if (dayCursor.moveToFirst()) {
            do {
                long dayId = dayCursor.getLong(0);
                String title = dayCursor.getString(1);
                DayTemplate day = new DayTemplate(dayId, title);

                Cursor exCursor = db.rawQuery("SELECT e.id, e.name, e.base_weight FROM exercises e JOIN day_exercises de ON e.id = de.exercise_id WHERE de.day_id = ?", new String[]{String.valueOf(dayId)});

                if (exCursor.moveToFirst()) {
                    do {
                        day.addExercise(new Exercise(exCursor.getLong(0), exCursor.getString(1), exCursor.getDouble(2)));
                    } while (exCursor.moveToNext());
                }
                exCursor.close();
                dayList.add(day);
            } while (dayCursor.moveToNext());
        }
        dayCursor.close();
        db.close();
        return dayList;
    }

    //ШАБЛОНЫ ПРОГРАММ
    public long addProgramTemplate(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO program_templates (title) VALUES (?)", new Object[]{title});

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        long id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getLong(0);
        }
        cursor.close();
        db.close();
        return id;
    }

    public void addDayToProgram(long programId, long dayId, int order) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO program_days (program_id, day_id, sequence_order) VALUES (?, ?, ?)", new Object[]{programId, dayId, order});
        db.close();
    }

    public void deleteProgramTemplate(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM program_templates WHERE id = ?", new Object[]{id});
        db.close();
    }

    public List<ProgramTemplate> getAllProgramTemplates() {
        List<ProgramTemplate> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id, title FROM program_templates", null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);

                com.example.trainingplanner.model.entities.ProgramTemplate program =
                        new com.example.trainingplanner.model.entities.ProgramTemplate(title);
                program.setId(id);

                Cursor dayCursor = db.rawQuery(
                        "SELECT dt.id, dt.title FROM day_templates dt JOIN program_days pd ON dt.id = pd.day_id WHERE pd.program_id = ? ORDER BY pd.sequence_order ASC",
                        new String[]{String.valueOf(id)});

                if (dayCursor.moveToFirst()) {
                    do {
                        program.addDay(new DayTemplate(dayCursor.getLong(0), dayCursor.getString(1)));
                    } while (dayCursor.moveToNext());
                }
                dayCursor.close();

                list.add(program);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    //СОЗДАНИЕ ЦИКЛА
    public void generateNewCycle(ProgramTemplate template, List<CyclePhase> phases) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();

        clearActiveCycle();

        db.execSQL("INSERT INTO active_cycle (program_title) VALUES (?)", new Object[]{template.getProgramName()});

        Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
        long cycleId = -1;
        if (cursor.moveToFirst()) {
            cycleId = cursor.getLong(0);
        }
        cursor.close();

        int weekCounter = 1;

        for (CyclePhase phase : phases) {

            for (int w = 0; w < phase.getWeeks(); w++) {

                Cursor dayCursor = db.rawQuery(
                        "SELECT dt.id, dt.title FROM day_templates dt JOIN program_days pd ON dt.id = pd.day_id WHERE pd.program_id = ? ORDER BY pd.sequence_order ASC",
                        new String[]{String.valueOf(template.getId())});

                if (dayCursor.moveToFirst()) {
                    do {
                        long templateDayId = dayCursor.getLong(0);
                        String dayTitle = dayCursor.getString(1);

                        db.execSQL("INSERT INTO active_days (cycle_id, week_number, title, is_completed) VALUES (?, ?, ?, 0)",
                                new Object[]{cycleId, weekCounter, dayTitle});

                        Cursor activeDayCursor = db.rawQuery("SELECT last_insert_rowid()", null);
                        long activeDayId = -1;
                        if (activeDayCursor.moveToFirst()) {
                            activeDayId = activeDayCursor.getLong(0);
                        }
                        activeDayCursor.close();

                        Cursor exCursor = db.rawQuery(
                                "SELECT e.name, e.base_weight FROM exercises e JOIN day_exercises de ON e.id = de.exercise_id WHERE de.day_id = ?",
                                new String[]{String.valueOf(templateDayId)});

                        if (exCursor.moveToFirst()) {
                            do {
                                String exName = exCursor.getString(0);
                                double baseWeight = exCursor.getDouble(1);

                                double calculatedWeight = baseWeight * (phase.getPercentage() / 100.0);

                                calculatedWeight = Math.round(calculatedWeight);

                                db.execSQL("INSERT INTO active_exercises (day_id, exercise_name, plan_weight, plan_reps, actual_weight, actual_reps) VALUES (?, ?, ?, ?, ?, ?)",
                                        new Object[]{activeDayId, exName, calculatedWeight, phase.getReps(), calculatedWeight, phase.getReps()});

                            } while (exCursor.moveToNext());
                        }
                        exCursor.close();

                    } while (dayCursor.moveToNext());
                }
                dayCursor.close();

                weekCounter++;
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

    }

    public void clearActiveCycle() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM active_exercises");
        db.execSQL("DELETE FROM active_days");
        db.execSQL("DELETE FROM active_cycle");
    }

    //ТЕКУЩИЙ ЦИКЛ
    public String getActiveCycleProgramTitle() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT program_title FROM active_cycle", null);
        String title = null;
        if (cursor.moveToFirst()) {
            title = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return title;
    }

    public int getMaxWeeksInActiveCycle() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(week_number) FROM active_days", null);
        int maxWeeks = 0;
        if (cursor.moveToFirst()) {
            maxWeeks = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return maxWeeks;
    }

    public List<ActiveDay> getActiveDaysWithExercisesForWeek(int weekNum) {
        List<ActiveDay> dayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor dayCursor = db.rawQuery(
                "SELECT id, title, is_completed FROM active_days WHERE week_number = ? ORDER BY id ASC",
                new String[]{String.valueOf(weekNum)});

        if (dayCursor.moveToFirst()) {
            do {
                long dayId = dayCursor.getLong(0);
                String title = dayCursor.getString(1);
                int isCompletedInt = dayCursor.getInt(2);

               ActiveDay activeDay = new ActiveDay(dayId, title, weekNum);
               activeDay.setCompleted(isCompletedInt == 1);

                Cursor exCursor = db.rawQuery(
                        "SELECT exercise_name, plan_weight, plan_reps FROM active_exercises WHERE day_id = ? ORDER BY id ASC",
                        new String[]{String.valueOf(dayId)});

                if (exCursor.moveToFirst()) {
                    do {
                        String exName = exCursor.getString(0);
                        double weight = exCursor.getDouble(1);
                        int reps = exCursor.getInt(2);

                        activeDay.addExercise(new ActiveExercise(exName, weight, reps));
                    } while (exCursor.moveToNext());
                }
                exCursor.close();

                dayList.add(activeDay);
            } while (dayCursor.moveToNext());
        }
        dayCursor.close();
        db.close();
        return dayList;
    }

    //ТЕКУЩАЯ ТРЕНИРОВКА
    public List<ActiveExercise> getExercisesForActiveDay(long dayId) {
        List<ActiveExercise> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT exercise_name, plan_weight, plan_reps, actual_weight, actual_reps FROM active_exercises WHERE day_id = ? ORDER BY id ASC",
                new String[]{String.valueOf(dayId)});

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                double planWeight = cursor.getDouble(1);
                int planReps = cursor.getInt(2);
                double actualWeight = cursor.getDouble(3);
                int actualReps = cursor.getInt(4);

                ActiveExercise ex = new ActiveExercise(name, planWeight, planReps);
                ex.setActualWeight(actualWeight);
                ex.setActualReps(actualReps);

                list.add(ex);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void saveWorkoutResults(long dayId, List<ActiveExercise> exercises) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (ActiveExercise ex : exercises) {
            db.execSQL(
                    "UPDATE active_exercises SET actual_weight = ?, actual_reps = ? WHERE day_id = ? AND exercise_name = ?",
                    new Object[]{ex.getActualWeight(), ex.getActualReps(), dayId, ex.getName()}
            );
        }

        db.execSQL("UPDATE active_days SET is_completed = 1 WHERE id = ?", new Object[]{dayId});
    }


    //АНАЛИЗ
    public boolean hasNotFinishedDays() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM active_days WHERE is_completed = 0", null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return count > 0;
    }

    public List<ActiveExercise> getAllExercisesForAnalysis() {
        List<ActiveExercise> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT exercise_name, plan_weight, plan_reps, actual_weight, actual_reps FROM active_exercises", null
        );

        if (cursor.moveToFirst()) {
            do {
                ActiveExercise ex = new ActiveExercise(cursor.getString(0), cursor.getDouble(1), cursor.getInt(2));
                ex.setActualWeight(cursor.getDouble(3));
                ex.setActualReps(cursor.getInt(4));
                list.add(ex);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

}