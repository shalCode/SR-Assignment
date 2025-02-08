package com.company;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App 
{
    public static void main (String[] args )
    {
        try {
            InputStream is = App.class.getResourceAsStream("/Data.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            List<List<String>> dataList = new ArrayList<List<String>>();
            Map<Integer, Integer> employeeManagerMap = new HashMap<Integer, Integer>();
            Map<Integer, List<Integer>> managerDirectReportiesMap = new HashMap<Integer, List<Integer>>(); 
            Map<Integer, List<Integer>> managerMap = new HashMap<Integer, List<Integer>>();         
            Map<Integer, Integer> reporteeCountMap = new HashMap<>();

            dataList = br.lines().map(line-> Arrays.asList((line.replace("\"", "")).split(","))).collect(Collectors.toList());
            List<List<String>> filteredDataList = filterDataList(dataList);
            employeeManagerMap = getEmployeeManagerMap(filteredDataList);
            managerDirectReportiesMap = getManagerDirectReportiesMap(employeeManagerMap);
            compareManagerSalaryWithDirectReporties(managerDirectReportiesMap, filteredDataList);
            reporteeCountMap = getEmployeeReporteeCount(employeeManagerMap, managerDirectReportiesMap);
            for(Map.Entry<Integer, Integer> map  : reporteeCountMap.entrySet()){
               if(map.getValue() > 4){
                    System.out.println("Manager with manager Id : " + map.getKey() + " has more than 4 reportees");
               }
             }
            }catch(Exception  e){
                e.printStackTrace();
            }  
    } 

    private static List<List<String>> filterDataList(List<List<String>> dataList){
        List<List<String>> filteredDataList = new ArrayList<List<String>>();
        for(List<String> data : dataList){
            if(!data.get(0).contains("Id")){
                filteredDataList.add(data);
            }
        }
        return filteredDataList;
    }

    private static  Map<Integer, Integer> getEmployeeManagerMap(List<List<String>> filteredDataList){
        Map<Integer, Integer> employeeManagerMap = new HashMap<Integer, Integer>();
        for(List<String> data : filteredDataList){
            int empId = Integer.parseInt(data.get(0));
            int mngrId = 0;
            if(data.size() > 4 ){
                mngrId = Integer.parseInt(data.get(4));
            }
            
            employeeManagerMap.put(empId,mngrId);
        }
        /*for(Map.Entry<Integer, Integer> map  : employeeManagerMap.entrySet()){
            System.out.println(map.getKey() + "-" + map.getValue());

         }*/
        return employeeManagerMap;
    }

    
    private static Map<Integer, List<Integer>> getManagerDirectReportiesMap(Map<Integer, Integer> employeeManagerMap) {
        Map<Integer, List<Integer>> managerDirectReportiesMap = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : employeeManagerMap.entrySet()) {
            if (entry.getKey().equals(entry.getValue())) {
                continue;
            }
            if(entry.getValue() != 0){
      
                if (managerDirectReportiesMap.containsKey(entry.getValue())) {
                    managerDirectReportiesMap.get(entry.getValue()).add(entry.getKey());
                } else {
                    List<Integer> employees = new ArrayList<Integer>();
                    employees.add(entry.getKey());
                    managerDirectReportiesMap.put(entry.getValue(), employees);
                }
            }
        }

        /*for(Map.Entry<Integer, List<Integer>> map  : managerDirectReportiesMap.entrySet()){
            System.out.println(map.getKey() + "-" + map.getValue());
         }*/

        return managerDirectReportiesMap;
    }

    public static void compareManagerSalaryWithDirectReporties(Map<Integer, List<Integer>> managerDirectReportiesMap, List<List<String>> filteredDataList){
        for(Map.Entry<Integer, List<Integer>> map  : managerDirectReportiesMap.entrySet()){
            int mngrId = map.getKey();
            List<Integer> directReportiesIds = map.getValue();
            int mngrSalary = 0;
            int directReportiesSumSalary = 0;
            int directReportiesAvgSalary = 0;
            int twentyPercentDirectReportiesAvgSalary = 0;
            int fiftyPercentDirectReportiesAvgSalary = 0;
            for(List<String> data : filteredDataList)
            {
                if(Integer.parseInt(data.get(0)) == mngrId){
                    mngrSalary = Integer.parseInt(data.get(3));
                }
                for(Integer directReportiesId : directReportiesIds){
                    if(Integer.parseInt(data.get(0)) == directReportiesId){
                         directReportiesSumSalary += Integer.parseInt(data.get(3));
                    }
                }         
            }
            directReportiesAvgSalary = directReportiesSumSalary/directReportiesIds.size();
            twentyPercentDirectReportiesAvgSalary = directReportiesAvgSalary/5;
            fiftyPercentDirectReportiesAvgSalary = directReportiesAvgSalary/2;
            if((mngrSalary - (directReportiesAvgSalary + twentyPercentDirectReportiesAvgSalary)) < 0){
                System.out.println("Manager with Id : " + mngrId + " has less salary by " + (mngrSalary - (directReportiesAvgSalary + twentyPercentDirectReportiesAvgSalary))); 
            }
            if((mngrSalary - (directReportiesAvgSalary + fiftyPercentDirectReportiesAvgSalary)) > 0){
                System.out.println("Manager with Id : "  + mngrId + " has more salary by " + (mngrSalary - (directReportiesAvgSalary + fiftyPercentDirectReportiesAvgSalary))); 
            }
        }
    }

    public static Map<Integer, Integer> getEmployeeReporteeCount(Map<Integer, Integer> employeeManagerMap, Map<Integer, List<Integer>> managerDirectReportiesMap) {
        Map<Integer, Integer> reporteeCountMap = new HashMap<>();
        for (Integer employee : employeeManagerMap.keySet()) {
            calculateEmployeeReportee(employee, managerDirectReportiesMap, reporteeCountMap);
        }
        return reporteeCountMap;
    }

    private static void calculateEmployeeReportee(Integer manager, Map<Integer, List<Integer>> managerDirectReportiesMap, Map<Integer, Integer> reporteeCountMap) {

        if (!managerDirectReportiesMap.containsKey(manager)) {
            reporteeCountMap.put(manager, 0);
            return;
        }

        int totalReportee = managerDirectReportiesMap.get(manager).size();

        for (Integer reportee : managerDirectReportiesMap.get(manager)) {
            if (!reporteeCountMap.containsKey(reportee)) {
            calculateEmployeeReportee(reportee, managerDirectReportiesMap, reporteeCountMap);
            }

            totalReportee += reporteeCountMap.get(reportee);
        }

        reporteeCountMap.put(manager, totalReportee);
    }
    
}
