import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ExpenseService {
    private final HashMap<Integer, Expense> expenses;
    private final HashMap<String , List<Expense>> userExpenses;
    private final HashMap<String,List<BalanceEntry>> groupExpenses;
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
       double splitamount = expense.getAmount()/(expense.getParticipants().size());
       for(String id : expense.getParticipants()) {

           this.groupExpenses.putIfAbsent(id, new ArrayList<>());
                   this.groupExpenses.get(id).add(
                           new BalanceEntry(id , expense.getUserId() , splitamount)
                   );
       }
       this.userExpenses.get(expense.getUserId()).add(expense);
    }
    public List<Expense> getExpensesPaidBy(String userId) {
        return this.userExpenses.get(userId);
    }
    public List<Expense> getAllExpenses() {
        return  new ArrayList<>(this.expenses.values());
    }
    public List<BalanceEntry> getEntries(String userId) {
        return this.groupExpenses.getOrDefault(userId,new ArrayList<>());
    }
}
