package burundi.ilucky.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import burundi.ilucky.model.Gift;

@Service
public class GiftService {

    public static Map<String, Gift> gifts;

    static {
        gifts = new HashMap<>();
        gifts.put("10000VND", new Gift("10000VND", "10.000 VND", 10000, "VND", 1));
        gifts.put("1000VND", new Gift("1000VND", "1.000 VND", 1000, "VND", 2));
        gifts.put("500VND", new Gift("500VND", "500 VND", 500, "VND", 3));
        gifts.put("200VND", new Gift("200VND", "200 VND", 200, "VND", 5));

        // Mete: Mảnh ghép
        gifts.put("SAMSUNG1", new Gift("SAMSUNG1", "Mảnh Samsung 1", 1, "SAMSUNG", 7));
        gifts.put("SAMSUNG2", new Gift("SAMSUNG2", "Mảnh Samsung 2", 1, "SAMSUNG", 7));
        gifts.put("SAMSUNG3", new Gift("SAMSUNG3", "Mảnh Samsung 3", 1, "SAMSUNG", 5));
        gifts.put("SAMSUNG4", new Gift("SAMSUNG4", "Mảnh Samsung 4", 1, "SAMSUNG", 7));

        // Let: Chữ cái
        gifts.put("L", new Gift("L", "1 Chữ cái \"L\"", 1, "PIECE", 5));
        gifts.put("I", new Gift("I", "1 Chữ cái \"I\"", 1, "PIECE", 2));
        gifts.put("T", new Gift("T", "1 Chữ cái \"T\"", 1, "PIECE", 5));
        gifts.put("E", new Gift("E", "1 Chữ cái \"E\"", 1, "PIECE", 5));

        gifts.put("SHARE", new Gift("SHARE", "Chia sẻ cho bạn bè để nhận được 1 lượt chơi", 1, "SHARE", 8));

        gifts.put("1STARS", new Gift("1STARS", "1 Sao", 1, "STARS", 10));
        gifts.put("2STARS", new Gift("2STARS", "2 Sao", 2, "STARS", 8));
        gifts.put("3STARS", new Gift("3STARS", "3 Sao", 3, "STARS", 6));
        gifts.put("4STARS", new Gift("4STARS", "4 Sao", 4, "STARS", 4));

        gifts.put("UNLUCKY", new Gift("UNLUCKY", "Chúc bạn may mắn lần sau", 1, "UNLUCKY", 9));
    }

    // public static Gift getRandomGift() {
    // List<String> keys = new ArrayList<>(gifts.keySet());

    // Random random = new Random();

    // String randomKey = keys.get(random.nextInt(keys.size()));

    // return gifts.get(randomKey);
    // }

    public static Gift getRandomGift() {
        List<String> keys = new ArrayList<>(gifts.keySet());
        List<Integer> rates = new ArrayList<>();

        for (String key : keys) {
            rates.add(gifts.get(key).getRate());
        }

        String randomKey = randomByWeight(keys, rates);
        return gifts.get(randomKey);
    }

    private static String randomByWeight(List<String> values, List<Integer> weights) {
        int total = 0;
        for (Integer weight : weights) {
            total += weight;
        }

        double rand = Math.random() * total;

        int cursor = 0;
        for (int i = 0; i < weights.size(); i++) {
            cursor += weights.get(i);
            if (cursor >= rand) {
                return values.get(i);
            }
        }

        return values.get(0);
    }
}
