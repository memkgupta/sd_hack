import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ExpenseService {
    private final HashMap<Integer, Expense> expenses;
    private final HashMap<Integer , List<Expense>> userExpenses;
    private final HashMap<Integer,List<Expense>> groupExpenses;
    public ExpenseService() {
        this.groupExpenses = new HashMap<>();
        userExpenses = new HashMap<>();
        expenses = new HashMap<>();
    }
    public void addExpense(Expense expense) {
        this.expenses.put(expense.getId(), expense);
       this.userExpenses.putIfAbsent(
               expense.getUserId(),new ArrayList<>()
       );
       for(Integer id : expense.getParticipants()) {
           this.groupExpenses.putIfAbsent(id, new ArrayList<>());
                   this.groupExpenses.get(id).add(expense);
       }
       this.userExpenses.get(expense.getUserId()).add(expense);
    }
    public List<Expense> getExpensesPaidBy(int userId) {
        return this.userExpenses.get(userId);
    }
    public List<Expense> getAllExpenses() {
        return  new ArrayList<>(this.expenses.values());
    }

}
