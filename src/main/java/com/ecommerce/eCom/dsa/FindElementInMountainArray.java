package com.ecommerce.eCom.dsa;

public class FindElementInMountainArray {

    public static void main(String[] args){

        int[] arr = {1,4,5,6,3,2,0};
        int target = 9;
        System.out.println(search(arr,target));
    }

    static int search(int[] arr, int target){
        int peak = findPeakIndexInMountain(arr);
        int firstTry = orderAgnosticBinarySearch(arr,target,0,peak);
        if(firstTry != -1){
            return firstTry;

        }
        return orderAgnosticBinarySearch(arr,target,peak, arr.length-1);
    }

    static int findPeakIndexInMountain(int[] arr) {
        int start = 0;
        int end = arr.length -1;

        while(start < end ){
            int mid = start + (end -start) / 2;
            if(arr[mid] > arr[mid +1]){
                end = mid;
            }else {
                start = mid +1;
            }
        }
        return start;

    }

    static int orderAgnosticBinarySearch(int[] arr, int target, int start, int end) {
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



