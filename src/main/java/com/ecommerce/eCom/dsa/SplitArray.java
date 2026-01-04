package com.ecommerce.eCom.dsa;

public class SplitArray {
    public static void main(String[] args){
        int [] nums = {7,2,5,8,10};
        int m = 2;
        System.out.println(splitArray(nums,m));
    }

    private static int splitArray(int[] nums, int m) {
        int start = 0;
        int end = 0;
        int arrLength = nums.length;
        for(int i =0; i < arrLength; i++){
            //In the end of the loop this will contain the max item of the array;
            start = Math.max(start,nums[i]);
            System.out.println("Minimum value of the array range :"+start);

            // Maximum possible value of the array : sum of all values
            end = end + nums[i];
            System.out.println("Maximum value of the array range :"+end);

        }
        while (start < end){
            //Try for the middle as potential answer.
            int mid = start + (end-start)/2;
            System.out.println("Largest sum of the array: " + mid);

            //Find how many pieces you can divide this in with max value
            int sum = 0;
            int pieces = 1;

            for(int num:nums){
                if(sum + num > mid){
                    //You can not add this number to this array, make a new one
                    // Add this to a new sub array sum == num
                    sum = num;
                    pieces = pieces +1;
                }else {
                    sum += num;
                }
            }

            System.out.println("Pieces: "+ pieces);

            if( pieces > m){
                start = mid +1;
            }else {
                end = mid;
            }
        }
        return start;

    }
}
