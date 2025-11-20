data class Lesson(val subject: String, val time: String)
var lessons = mutableListOf<Lesson>()

interface LessonRepository {
    fun addLesson(subject: String, time: String)
    fun removeLesson(subject: String)
    fun hasGaps(): Boolean
}

class TimetableEditor : LessonRepository {
    override fun addLesson(subject: String, time: String) {
        if (hasGaps()) {
            lessons.add(Lesson(subject, time))
        }
    }

    override fun removeLesson(subject: String) {
        lessons.removeIf { it.subject == subject }
    }

    override fun hasGaps(): Boolean {
        // очень условная проверка «дырок»
        // считаем, что если уроков меньше 4 за день — есть "дыры"
        return lessons.size < 4
    }
}

class TimetableNotifications {
    fun notifyStudentsIfChanged(studentEmails: List<String>) {
        println("Расписание изменено, отправляю письма:")
        for (email in studentEmails) {
            println("Email на $email: Расписание уроков обновлено!")
        }
    }
}

class Lessons {
    fun printTimetable() {
        println("Текущее расписание:")
        for (lesson in lessons) {
            println("${lesson.time}: ${lesson.subject}")
        }
    }
}

fun main() {
    val lesson = TimetableEditor()
    lesson.addLesson("math", "12.25")
    lesson.addLesson("informatics", "12.25")
    val timetable = Lessons()
    println(timetable.printTimetable())
}