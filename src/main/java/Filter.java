import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Filter
{
    public Set<Project> filterByEmployeeName(Set<Project> projects, String employeeName){

        Predicate<Employee> nameContainsPattern = employee -> employee.getName().toLowerCase().contains(employeeName.toLowerCase());
        boolean employeeNameIsNotEmpty = !employeeName.equals("");

        if(employeeNameIsNotEmpty){

//            Set<Project> result = projects
//                    .stream()
//                    .filter(project -> {
//                        return project.getEmployees()
//                                .stream()
//                                .anyMatch(nameContainsPattern);
//                    })
//                    .collect(Collectors.toSet());
//
//            result.stream()
//                    .forEach(project -> project.getEmployees().removeIf(nameContainsPattern.negate()));
//
//            return result;

            return copyProjects(projects, employeeName);
        }

        return projects;
    }

    private Set<Project> copyProjects(Set<Project> projects, String employeeName){

        Set<Project> result = new HashSet<>();

        for(Project project : projects){
            if(project.getEmployees().stream().anyMatch(employee -> employee.getName().toLowerCase().contains(employeeName.toLowerCase()))){
                Project newProject = new Project();
                newProject.setName(project.getName());

                Set<Employee> selectedEmployee = project.getEmployees().stream().filter(employee -> employee.getName().toLowerCase()
                        .contains(employeeName.toLowerCase())).collect(Collectors.toSet());

                newProject.setEmployees(selectedEmployee);

                for(Employee employee : selectedEmployee){
                    for(Task task : employee.getTasks()){
                        newProject.addTask(task);
                    }
                }
                result.add(newProject);
            }
        }

        return result;
    }

    public Set<Project> filterByTaskName(Set<Project> projects, String taskName){

        Predicate<Task> nameContainsPattern = task -> task.getDescription().toLowerCase().contains(taskName.toLowerCase());
        boolean isTaskNameNotEmpty = !taskName.equals("");

        if(isTaskNameNotEmpty){

            Set<Project> result = projects
                    .stream()
                    .filter(project -> {
                        return project.getTasks()
                                .stream()
                                .anyMatch(nameContainsPattern);
                    })
                    .collect(Collectors.toSet());

            result.stream()
                    .forEach(project -> project.getTasks().removeIf(nameContainsPattern.negate()));

            result.stream()
                    .forEach(project -> project.getEmployees().forEach(employee -> employee.getTasks().removeIf(nameContainsPattern.negate())));

            return result;
        }

        return projects;
    }



    public Set<Project> filterByDate(Set<Project> projects, String userDateFrom, String userDateTo){

        boolean userDateIsNotEmpty = !userDateFrom.equals("") || !userDateTo.equals("");

        if(userDateIsNotEmpty){
            Predicate<Task> finalDateIsBetween = dataIsBetweenUserValues(userDateFrom, userDateTo);
            Set<Project> result = projects
                    .stream()
                    .filter(project -> {
                        return project.getTasks()
                                .stream()
                                .anyMatch(finalDateIsBetween);
                    })
                    .collect(Collectors.toSet());

            result.stream()
                    .forEach(project -> project.getTasks().removeIf(finalDateIsBetween.negate()));

            result.stream()
                    .forEach(project -> project.getEmployees().stream().forEach(employee->employee.getTasks().removeIf(finalDateIsBetween.negate())));

            return result;
        }
        return projects;
    }

    private LocalDate getDateFromString(String dateString){

        if(dateString.equals(""))
            dateString = "1.01.2000";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
        return LocalDate.parse(dateString, formatter);
    }


    private Predicate<Task> dataIsBetweenUserValues(String userDateFrom, String userDateTo)
    {
        LocalDate dateFrom = getDateFromString(userDateFrom);
        LocalDate dateTo = getDateFromString(userDateTo);

        Predicate<Task> dateIsBetween = task -> (task.getDate().isAfter(dateFrom) || task.getDate().isEqual(dateFrom))
                && (task.getDate().isBefore(dateTo) || task.getDate().isEqual(dateTo));

        if(userDateTo.equals("")){
            dateIsBetween = task -> task.getDate().isAfter(dateFrom) || task.getDate().isEqual(dateFrom);
        }

        if(userDateFrom.equals("")){
            dateIsBetween = task -> task.getDate().isBefore(dateTo) || task.getDate().isEqual(dateTo);
        }
        return dateIsBetween;
    }
}
