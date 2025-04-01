package re1kur.mailService.client;

import java.util.List;

public interface DatabaseClient {
    List<String> getSubscribersEmails(String objectChangesName, Integer objectChangesId);
}
