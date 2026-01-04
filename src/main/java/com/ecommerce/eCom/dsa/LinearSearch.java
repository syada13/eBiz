package com.ecommerce.eCom.dsa;

public class LinearSearch {

    public static void main(String[] args){
        int[] nums = {1,3,5,2,14,0};
        int target = 5;
        int start = 1;
        int end = 4;
        String str = "Suresh";
        char targetCharacter = 'h';
        //System.out.println(searchInString(str,targetCharacter));
        //System.out.println(searchInRange(nums,start, end,target));

        System.out.println(minimumNumber(nums));
    }

    static int minimumNumber(int[] nums){
        if(nums.length == 0){
            return Integer.MIN_VALUE;
        }

        int minNumber = nums[0];

        for(int index = 1; index < nums.length; index++){
            if(nums[index] < minNumber){
                minNumber = nums[index];
            }
        }

        return  minNumber;
    }

    static int linearSearch(int[] arr, int target){
        int arrLength = arr.length;

        if(arrLength == 0){
            return -1;
        }

        for(int i =0; i < arrLength; i++){
            if(arr[i] == target){
                return i;
            }
        }
        return -1;
    }

    static int linearSearchValue(int[] arr, int target){
        int arrLength = arr.length;
        if(arrLength == 0){
            return Integer.MAX_VALUE;
        }

        for(int num: arr){
            if(num == target){
                return target;
            }
        }
        return Integer.MAX_VALUE;
    }

    static boolean searchInString(String str, char targetCharacter){

        if(str.isEmpty()){
            return false;
        }

        for(int i = 0; i < str.length(); i++){
            if(targetCharacter == str.charAt(i)){
                return true;
            }
        }
        return false;
    }

    static int searchInRange(int[] nums, int start, int end,int target){

        if(nums.length == 0){
            return -1;
        }

        for(int index= start; index <=end; index++){
            if(target == nums[index]){
                return index;
            }
        }
        return -1;

    }
}
