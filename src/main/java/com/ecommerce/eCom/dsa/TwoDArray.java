package com.ecommerce.eCom.dsa;

public class TwoDArray {
    public static void main(String[] args) {
        int[][] arr ={
                {1,3,4},
                {19,8,44},
                {6,99}
        };

        int target = 44;
        //System.out.println(search(arr));
        System.out.println(maximumWealth(arr));
        System.out.println(wealthiestPerson(arr));
    }

    static int search(int[][] arr){

        if(arr.length == 0){
            return -1;
        }

        int max = arr[0][0];
        for (int row =0; row < arr.length; row++){
           for(int col =0; col < arr[row].length; col++){
               if(arr[row][col] > max){
                   //return new int[]{row,col};
                  max = arr[row][col];
               }
           }

        }
        //return new int[]{-1,-1};
        return max;
    }

    static int maximumWealth(int[][] arr){
        int answer = Integer.MIN_VALUE;
        for( int row =0; row < arr.length; row++){
            int sum = 0;
            for(int col = 0; col < arr[row].length;col++){
                sum = sum + arr[row][col];
            }

            if(sum > answer){
                answer = sum;
            }
        }

        return answer;
    }

    static int wealthiestPerson(int[][] arr){

        int answer = Integer.MIN_VALUE;
        int maxSumRow = -1;
        for( int row =0; row < arr.length; row++){
            int sum = 0;
            for(int col = 0; col < arr[row].length;col++){
                sum = sum + arr[row][col];

            }
            if(sum > answer){
                answer = sum;
                maxSumRow = row;
            }
        }

        return maxSumRow;
    }
}

