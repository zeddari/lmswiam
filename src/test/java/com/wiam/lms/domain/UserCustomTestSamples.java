package com.wiam.lms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserCustomTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserCustom getUserCustomSample1() {
        return new UserCustom()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .code("code1")
            .accountName("accountName1")
            .phoneNumber1("phoneNumber11")
            .phoneNumver2("phoneNumver21")
            .facebook("facebook1")
            .telegramUserCustomId("telegramUserCustomId1")
            .telegramUserCustomName("telegramUserCustomName1");
    }

    public static UserCustom getUserCustomSample2() {
        return new UserCustom()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .code("code2")
            .accountName("accountName2")
            .phoneNumber1("phoneNumber12")
            .phoneNumver2("phoneNumver22")
            .facebook("facebook2")
            .telegramUserCustomId("telegramUserCustomId2")
            .telegramUserCustomName("telegramUserCustomName2");
    }

    public static UserCustom getUserCustomRandomSampleGenerator() {
        return new UserCustom()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .code(UUID.randomUUID().toString())
            .accountName(UUID.randomUUID().toString())
            .phoneNumber1(UUID.randomUUID().toString())
            .phoneNumver2(UUID.randomUUID().toString())
            .facebook(UUID.randomUUID().toString())
            .telegramUserCustomId(UUID.randomUUID().toString())
            .telegramUserCustomName(UUID.randomUUID().toString());
    }
}
