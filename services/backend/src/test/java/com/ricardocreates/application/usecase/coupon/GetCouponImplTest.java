package com.ricardocreates.application.usecase.coupon;

import com.ricardocreates.application.validator.CouponValidator;
import com.ricardocreates.domain.entity.administrator.Administrator;
import com.ricardocreates.domain.entity.coupon.Coupon;
import com.ricardocreates.domain.entity.coupon.LocationProduct;
import com.ricardocreates.domain.repository.administrator.AdministratorRepository;
import com.ricardocreates.domain.repository.coupon.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GetCouponImplTest {
    private static final String COUPON_NAME = "name";
    private static final String COUPON_CODE = "code";
    private static final String ADMINISTRATOR_ID = "1";
    private static final String ADMINISTRATOR_LOGIN = "login";
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private AdministratorRepository administratorRepository;
    @Mock
    private CouponValidator couponValidator;
    @InjectMocks
    private GetCouponImpl getCoupon;

    @Test
    void should_get_coupons() {
        // Given
        String login = "login";
        Coupon coupon1 = mockCoupon();
        Coupon coupon2 = mockCoupon().toBuilder().code("code2").name("name2").build();
        List<Coupon> couponList = List.of(coupon1, coupon2);
        given(this.couponRepository.findAll()).willReturn(Flux.fromIterable(couponList));

        // When
        var response = this.getCoupon.findAll(login).collectList().block();

        // Then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(couponList.size());
        verify(this.couponRepository).findAll();
    }

    @Test
    void should_get_coupons_withLocations() {
        // Given
        String login = "login";
        Coupon coupon1 = mockCoupon();
        coupon1.setLocationProducts(mockLocationProducts(0, 2));
        Coupon coupon2 = mockCoupon().toBuilder().code("code2").name("name2").build();
        coupon2.setLocationProducts(mockLocationProducts(3, 5));
        List<Coupon> couponList = List.of(coupon1, coupon2);
        given(this.couponRepository.findAll()).willReturn(Flux.fromIterable(couponList));

        // When
        var response = this.getCoupon.findAll(login).collectList().block();

        // Then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(couponList.size());
        verify(this.couponRepository).findAll();
    }

    @Test
    void should_get_coupon_by_id() {
        // Given
        String login = "login";
        String id = "id";
        Coupon coupon = mockCoupon();
        given(this.couponRepository.getById(eq(id))).willReturn(Mono.just(coupon));

        // When
        var response = this.getCoupon.get(id, login).block();

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(coupon.getName());
        verify(this.couponRepository).getById(eq(id));
    }

    private Coupon mockCoupon() {
        return Coupon.builder()
                .name(COUPON_NAME)
                .code(COUPON_CODE)
                .build();
    }

    private Administrator mockAdministrator() {
        Administrator admin = Administrator.builder().build();
        admin.setId(ADMINISTRATOR_ID);
        admin.setLogin(ADMINISTRATOR_LOGIN);
        admin.setPassword("test");
        return admin;
    }

    private LocationProduct mockLocationProduct(String idLocation) {
        return LocationProduct.builder()
                .idLocation(idLocation)
                .build();
    }

    private List<LocationProduct> mockLocationProducts(int min, int max) {
        List<LocationProduct> list = new ArrayList<>();
        for (int index = min; index < max; index++) {
            list.add(mockLocationProduct("" + index));
        }
        return list;
    }
}