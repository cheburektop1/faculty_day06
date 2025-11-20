data class Lesson(val subject: String, val time: String)
var lessons = mutableListOf<Lesson>()
class TimetableEditor {
    // private var lessons = mutableListOf<Lesson>()

    fun addLesson(subject: String, time: String) {
        if (hasGaps()) {
            lessons.add(Lesson(subject, time))
        }
    }

    fun removeLesson(subject: String) {
        lessons.removeIf { it.subject == subject }
    }

    fun hasGaps(): Boolean {
        // очень условная проверка «дырок»
        // считаем, что если уроков меньше 4 за день — есть "дыры"
        return lessons.size < 4
    }
}

class TimetableNotifications {
    // private val lessons = mutableListOf<Lesson>()
    fun notifyStudentsIfChanged(studentEmails: List<String>) {
        println("Расписание изменено, отправляю письма:")
        for (email in studentEmails) {
            println("Email на $email: Расписание уроков обновлено!")
        }
    }
}

class Lessons {
    //public val lessons = mutableListOf<Lesson>()

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