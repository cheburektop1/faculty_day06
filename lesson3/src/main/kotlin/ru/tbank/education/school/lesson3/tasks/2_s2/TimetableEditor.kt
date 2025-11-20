data class Lesson(val subject: String, val time: String)

// 1. SRP - Single Responsibility Principle
// Каждый класс отвечает за одну задачу

interface LessonRepository {
    fun addLesson(lesson: Lesson)
    fun removeLesson(subject: String)
    fun getAllLessons(): List<Lesson>
}

class LessonRepositoryImpl : LessonRepository {
    private val lessons = mutableListOf<Lesson>()

    override fun addLesson(lesson: Lesson) {
        lessons.add(lesson)
    }

    override fun removeLesson(subject: String) {
        lessons.removeIf { it.subject == subject }
    }

    override fun getAllLessons(): List<Lesson> {
        return lessons.toList()
    }
}

// 2. OCP - Open/Closed Principle
// Классы открыты для расширения, но закрыты для модификации

interface GapChecker {
    fun hasGaps(lessons: List<Lesson>): Boolean
}

class BasicGapChecker : GapChecker {
    override fun hasGaps(lessons: List<Lesson>): Boolean {
        return lessons.size < 4
    }
}

class AdvancedGapChecker(private val minLessons: Int) : GapChecker {
    override fun hasGaps(lessons: List<Lesson>): Boolean {
        return lessons.size < minLessons
    }
}

// 3. LSP - Liskov Substitution Principle
// Наследники могут заменять родителей

interface NotificationService {
    fun notifyStudents(studentEmails: List<String>)
}

class EmailNotificationService : NotificationService {
    override fun notifyStudents(studentEmails: List<String>) {
        println("Расписание изменено, отправляю письма:")
        for (email in studentEmails) {
            println("Email на $email: Расписание уроков обновлено!")
        }
    }
}

class ConsoleNotificationService : NotificationService {
    override fun notifyStudents(studentEmails: List<String>) {
        println("Уведомление для студентов:")
        studentEmails.forEach { email ->
            println("Студент $email: расписание обновлено!")
        }
    }
}

// 4. ISP - Interface Segregation Principle
// Интерфейсы разделены по функциональности

interface TimetableDisplay {
    fun printTimetable()
}

interface TimetableAnalysis {
    fun checkForGaps(): Boolean
}

// 5. DIP - Dependency Inversion Principle
// Зависимости от абстракций, а не от реализаций

class TimetableEditor(
    private val lessonRepository: LessonRepository,
    private val gapChecker: GapChecker,
    private val notificationService: NotificationService
) : TimetableDisplay, TimetableAnalysis {

    fun addLesson(subject: String, time: String) {
        lessonRepository.addLesson(Lesson(subject, time))
    }

    fun removeLesson(subject: String) {
        lessonRepository.removeLesson(subject)
    }

    override fun checkForGaps(): Boolean {
        return gapChecker.hasGaps(lessonRepository.getAllLessons())
    }

    fun notifyStudents(studentEmails: List<String>) {
        notificationService.notifyStudents(studentEmails)
    }

    override fun printTimetable() {
        val lessons = lessonRepository.getAllLessons()
        println("Текущее расписание:")
        lessons.forEach { lesson ->
            println("${lesson.time}: ${lesson.subject}")
        }

        if (checkForGaps()) {
            println("ВНИМАНИЕ: В расписании есть пропуски!")
        }
    }
}

// Пример использования
fun main() {
    val repository = LessonRepositoryImpl()
    val gapChecker = BasicGapChecker()
    val notificationService = EmailNotificationService()

    val timetableEditor = TimetableEditor(repository, gapChecker, notificationService)

    // Добавляем уроки
    timetableEditor.addLesson("Математика", "09:00")
    timetableEditor.addLesson("Физика", "10:30")
    timetableEditor.addLesson("Химия", "12:00")

    // Выводим расписание
    timetableEditor.printTimetable()

    // Уведомляем студентов
    timetableEditor.notifyStudents(listOf("student1@edu.ru", "student2@edu.ru"))

    // Проверяем наличие пропусков
    println("Есть ли пропуски: ${timetableEditor.checkForGaps()}")
}