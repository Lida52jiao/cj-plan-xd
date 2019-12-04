package com.yj.bj.service.user;




import com.yj.bj.entity.user.CardInformation;
import com.yj.bj.entity.user.MerChants;

import java.util.List;

/**
 * Created by bin on 2017/11/7.
 */
public interface MerChantsService {

    MerChants getMer(String merId, String merHost);

    MerChants getMerRate(String merId, String aisleCode, String merHost);

    CardInformation getCard(Long cardId, String merHost);

    List<CardInformation> getCardList(String merId, String cardType, String token, String merHost);
}
