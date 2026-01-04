package com.ecommerce.eCom.dsa;

public class FindElementInRotatedSortedArray {

    public static void main(String[] args){

        int[] arr = {3,4,5,6,0,1,2};
        //int[] arr = {2,2,2,9,2};
        int target = 3;
        System.out.println(findPivot(arr));
        //System.out.println(findPivotWithDuplicates(arr));
        System.out.println(search(arr,target));
    }

    static int search(int[] arr, int target){
        int pivot = findPivot(arr);

        if(pivot == -1){
            return binarySearch(arr,target,0,arr.length-1);
        }

        if(target == arr[pivot]){
            return pivot;
        }

        if(target >= arr[0]){
            return binarySearch(arr,target,0,pivot-1);

        }

        return binarySearch(arr,target,pivot+1,arr.length-1);
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

    static int findPivotWithDuplicates(int[] arr){
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
             //
            if(arr[mid] ==arr[start] && arr[mid] == arr[end]){
                if(arr[start] > arr[start+1]){
                    return start;
                }
                start++;

                if(arr[end]< arr[end-1]){
                    return end-1;
                }
                end--;

                // Left side is sorted , so pivot should be in right
            }else if(arr[start] < arr[mid] || (arr[start] == arr[mid] && arr[mid] > arr[end])){
                start = mid +1;
            }else{
                end = mid -1;
            }
        }
        return -1;
    }

    static int binarySearch(int[] arr, int target, int start, int end){
        while(start <= end){
            int mid = start + (end-start)/2;

            if(target == arr[mid]){
                return mid;
            }else if( target > arr[mid]){
                start = mid +1;
            }else{
                end = mid -1;
            }
        }
        return -1;

    }
}
