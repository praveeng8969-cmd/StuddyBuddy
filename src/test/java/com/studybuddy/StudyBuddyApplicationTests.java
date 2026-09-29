package com.studybuddy;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Requires running MySQL database with configured credentials")
class StudyBuddyApplicationTests
{
    @Test
    void contextLoads()
    {
    }
}
