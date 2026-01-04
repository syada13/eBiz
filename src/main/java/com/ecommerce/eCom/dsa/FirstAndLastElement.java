package com.ecommerce.eCom.dsa;

import java.util.Arrays;

public class FirstAndLastElement {
    public static void main(String[] args) {
        int[] ans = {-1, -1};
        int[] nums = {5, 7, 7, 7, 7, 8, 8, 10};
        int target = 8;
        boolean findingTargetFirstOccurance;
        ans[0] = binarySearch(nums, target, findingTargetFirstOccurance= true);
        ans[1] = binarySearch(nums, target, findingTargetFirstOccurance = false);
        System.out.print(Arrays.toString(ans));
    }

    static int binarySearch(int[] nums, int target, boolean findingTargetFirstOccurance) {
        int ans = -1;
        int start = 0;
        int end = nums.length - 1;

        while (start <= end) {
            int mid = start + (end - start) / 2;

            if (target > nums[mid]) {
                start = mid + 1;
            } else if (target < nums[mid]) {
                end = mid - 1;
            } else {
                // Potential answer found
                ans = mid;
                if(findingTargetFirstOccurance){
                    end = mid -1;
                }else{
                    start = mid +1;
                }
            }
        }
        return ans;
    }
}
