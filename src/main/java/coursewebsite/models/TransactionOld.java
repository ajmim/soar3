package coursewebsite.models;

import coursewebsite.exceptions.InsufficientBalanceException;
import coursewebsite.models.CourseOld;
import coursewebsite.models.Student;
import coursewebsite.models.Teacher;

public class TransactionOld {
    private final Student student;
    private final Teacher teacher;
    private final double amount;
    private final boolean success;

    TransactionOld(Student student, Teacher teacher, double amount) {
        this.student = student;
        this.teacher = teacher;
        this.amount= amount;

        if (student.getBalance() >= amount) {
            student.addBalance(-amount);
            teacher.addBalance(amount);
            student.userTransactions.add(this);
            teacher.userTransactions.add(this);
            this.success = true;
        } else {
            this.success = false;
        }
    }

    public boolean isTransactionSuccess() { return this.success; }

    public Student getTransactionStudent() { return this.student; }

    public Teacher getTransactionTeacher() { return this.teacher; }

    public double getTransactionAmount() { return this.amount; }

    @Override
    public String toString() {
        return "Transaction between: " + this.student.getFirstName() + " and " + this.teacher.getFirstName() + " = " + this.amount;
    }

}