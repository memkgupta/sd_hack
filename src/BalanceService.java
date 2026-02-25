import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class BalanceService {
private final ExpenseService expenseService ;
private final UserServiceInterface userService ;
    public BalanceService(ExpenseService expenseService, UserServiceInterface userService)
{
    this.expenseService = expenseService;
    this.userService = userService;
}
    public List<BalanceEntry> getBalance()
    {
       return finalSummary(
               getFinalUserBalance(
                       betweenUsers()
               )
       );
    }
    private List<BalanceEntry> finalSummary(HashMap<String,UserBalance> balances)
    {
        List<BalanceEntry> finalEntries = new ArrayList<>();
        PriorityQueue<UserBalance> maxCreditor = new PriorityQueue<>(
                (a,b)->{

                    return (int) (b.getGets() - a.getGets());
                }
        );
        PriorityQueue<UserBalance> maxDebitor = new PriorityQueue<>(
                (a,b)->{

                    return (int) (b.getOwes() - a.getOwes());
                }
        );

        for(UserBalance balance : balances.values())
        {
            if(balance.getOwes() > 0)
            {
                maxDebitor.add(balance);
            }
            else if(balance.getGets() > 0) {
                maxCreditor.add(balance);
            }
        }
        while(!maxCreditor.isEmpty() && !maxDebitor.isEmpty())
        {
            UserBalance creditor = maxCreditor.poll();
            UserBalance debitor = maxDebitor.poll();
            if(creditor.getGets()>= debitor.getOwes())
            {
                double amountPaid  = debitor.getOwes();
                double amountRemaining = creditor.getGets() - amountPaid;
                creditor.setGets(amountRemaining);
                debitor.setOwes(0);
                finalEntries.add(new BalanceEntry(
                        debitor.getUserId() , creditor.getUserId(), amountPaid
                ));
            }
            else {
                double amountPaid  = creditor.getGets();
                double amountRemaining = debitor.getOwes() - amountPaid;
                creditor.setGets(0);
                debitor.setOwes(amountRemaining);
                finalEntries.add(new BalanceEntry(
                        debitor.getUserId() , creditor.getUserId(), amountPaid
                ));
            }
            if(creditor.getGets()>0)
            {
                maxCreditor.offer(creditor);
            }
            if(debitor.getOwes()>0)
            {
                maxDebitor.offer(debitor);
            }
        }
        return finalEntries;
    }
    private HashMap<String,UserBalance> getFinalUserBalance(List<BalanceEntry> balanceEntries)
    {
        HashMap<String,UserBalance> finalUserBalance = new HashMap<>();
        for(BalanceEntry balanceEntry : balanceEntries)
        {
            String from = balanceEntry.getFrom();
            String to = balanceEntry.getTo();
            double amount = balanceEntry.getAmount();
            finalUserBalance.putIfAbsent(from,
                    new UserBalance(from,0.0,0.0));
            finalUserBalance.putIfAbsent(
                    to ,
                    new UserBalance(to,0.0,0.0)
            );

            finalUserBalance.get(from).setOwes(finalUserBalance.get(from).getOwes() + amount);
            finalUserBalance.get(to).setGets(finalUserBalance.get(from).getGets() + amount);
        }
        for(UserBalance userBalance : finalUserBalance.values())
        {
            double owe =  userBalance.getOwes();
            double gets = userBalance.getGets();
            if(owe >= gets)
            {
                userBalance.setOwes(userBalance.getOwes() - gets);
                userBalance.setGets(0);
            }
            else {
                userBalance.setOwes(0);
                userBalance.setGets(
                        userBalance.getGets() - owe
                );
            }
        }
        return finalUserBalance;
    }
    private List<BalanceEntry> betweenUsers()
    {
        List<User> allUsers = userService.getAllUsers();
        List<BalanceEntry> balanceEntries = new ArrayList<>();
        for(int i = 0;i<allUsers.size();i++)
        {
            User user = allUsers.get(i);
            for(int j = i+1;j<allUsers.size();j++)
            {
                User u2 =  allUsers.get(j);

                double u1Amount = expenseService.getEntries(user.getId()).stream()
                        .filter(e->e.getTo().equals(u2.getId()))
                        .mapToDouble(BalanceEntry::getAmount)
                        .reduce(0, Double::sum);
                double u2Amount = expenseService.getEntries(u2.getId()).stream()
                        .filter(e->e.getTo().equals(u2.getId()))
                        .mapToDouble(BalanceEntry::getAmount)
                        .reduce(0, Double::sum);

                BalanceEntry entry = new BalanceEntry();
                if(u1Amount > u2Amount)
                {
                    entry.setFrom(user.getId());
                    entry.setTo(u2.getId());
                }
                else {
                    entry.setFrom(u2.getId());
                    entry.setTo(user.getId());
                }
                entry.setAmount(Math.abs(u1Amount-u2Amount));
                balanceEntries.add(entry);

            }
        }
        return balanceEntries;
    }
}
