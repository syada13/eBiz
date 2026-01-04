package com.ecommerce.eCom.dsa;

public class OrderAgnosticBinarySearch {

    public static void main(String[] args){
        //int[] arr = {1,2,3,4,5,6,7};
        int[] arr = {7,6,5,4,3,2,1};
        int target = 6;
        System.out.println(orderAgnosticBinarySearch(arr,target));
    }

     static int orderAgnosticBinarySearch(int[] arr, int target) {
        int start = 0;
        int end = arr.length -1;
        while( start <= end){
            int mid = start + (end -start) / 2;
            if (target == arr[mid]){
                    return mid ;
            }

            boolean isAsc = arr[start] < arr[end];

            if(isAsc){
                if(target > arr[mid]){
                    start = mid +1;
                }else{
                    end = mid -1;
                }
            }else{
                if(target > arr[mid]){
                    end = mid -1;

                }else{
                    start = mid +1;
                }
            }

        }

        return -1;
    }
}
