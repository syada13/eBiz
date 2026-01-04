package com.ecommerce.eCom.dsa;

public class PeakIndexInMountain {

    public static void main(String[] args){
        int[] arr = {1,2,5,4,3,0};
        System.out.println(findPeakIndexInMountain(arr));
    }

     static int findPeakIndexInMountain(int[] arr) {
        int start = 0;
        int end = arr.length -1;

        while( start < end){
            int mid = start + (end-start)/2;
            if(arr[mid] < arr[mid+1]){
                start = mid +1;
            }else {
                end = mid;

            }
        }
         return start;
    }
}
