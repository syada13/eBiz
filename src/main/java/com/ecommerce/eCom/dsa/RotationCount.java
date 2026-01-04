package com.ecommerce.eCom.dsa;

public class RotationCount {

    public static void main(String[] args){
        int[] arr = {3,4,5,6,0,1,2};
        System.out.println(rotationsCount(arr));

    }

    static int rotationsCount(int[] arr){
        int pivotIndex = findPivot(arr);
        return pivotIndex +1;
    }

    static int findPivot(int[] arr){
        int start = 0;
        int end = arr.length-1;

        while(start <= end){
            int mid = start+(end -start)/2;

            if( mid < end && arr[mid] > arr[mid+1]){
                return mid;
            }

            if(mid > start && arr[mid] < arr[mid-1]){
                return mid -1;
            }

            if( arr[mid] <= arr[start]){
                end = mid -1;

            }else{
                start = mid +1;
            }
        }
        return -1;
    }

}
