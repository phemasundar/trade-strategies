package com.strategies.trade.copied;

import com.strategies.trade.api_test_beans.CustomResponse;
import com.strategies.trade.api_test_beans.VacsineResponse;
import com.strategies.trade.api_utils.ApiRequests;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
public class VaccineTests {

    @Test
    @Tested
    public void vaccine() throws IOException {

        CustomResponse<VacsineResponse> vacsineDetails = ApiRequests.getVaccineDetails();
        System.out.println(vacsineDetails.getResponseObj());
        List<VacsineResponse.Centers> centres_18 = vacsineDetails.getResponseObj()
                .getCenters()
                .stream()
                .filter(item -> item.getSessions().stream().anyMatch(item1 -> item1.getMin_age_limit() == 18))
                .collect(Collectors.toList());
        System.out.println("centres_18: " + centres_18.size() + "\n\t" + centres_18);
        List<VacsineResponse.Centers> centres_18_withVaccines_dose1 = centres_18.stream()
                .filter(item -> item.getSessions().stream().anyMatch(item1 -> item1.getAvailable_capacity_dose1() > 0))
                .collect(Collectors.toList());
        System.out.println("centres_18_withVaccines_dose1: " + centres_18_withVaccines_dose1.size() + "\n\t" + centres_18_withVaccines_dose1);

    }

}
