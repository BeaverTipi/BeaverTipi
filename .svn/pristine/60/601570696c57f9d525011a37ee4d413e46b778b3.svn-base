package kr.or.ddit.util.batch.idseq;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
@EnableEncryptableProperties
@SpringBootTest
public class ListingIdSequenceResetJobTest {

    @Autowired
    private ListingIdSequenceResetJob listingIdSequenceResetJob;

    @Test
    public void testManualExecution() {
        try {
            // null 전달해도 무방 (JobExecutionContext를 쓰지 않으니까)
            listingIdSequenceResetJob.execute(null);
            System.out.println("✅ 수동 실행 성공");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
