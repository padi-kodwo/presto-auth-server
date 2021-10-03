package com.presto.auth.util;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.presto.auth.domain.response.ApiResponse;
import com.presto.auth.domain.response.PagedContent;
import com.presto.auth.enums.ResponseMessage;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.bouncycastle.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings({"rawtypes", "unused"})
public class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    public static String generateDateTime() {
        DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormatUTC.format(new Date());
    }

    public static String getReleaseDateFromCalendar(Calendar calendar) {
        DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormatUTC.format(calendar.getTime());
    }

    public static String generateDateFromDaysPassed(int daysPassed) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -(daysPassed));
        Date date = calendar.getTime();

        DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormatUTC.format(date);
    }

    public static String generateExpiryDate(int daysFromToday) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, (daysFromToday));
        Date date = calendar.getTime();

        DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormatUTC.format(date);
    }


    public static Timestamp getTimestampFromDate(String invmDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = dateFormat.parse(invmDateString);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isTimeElapsed(long timeMillis) {
        return System.currentTimeMillis() > timeMillis;
    }

    public static String daysPassedFromDateCreated(String dateCreated) {

        if (isEmptyOrNull(dateCreated))
            return "Date not set";

        String datePart = dateCreated.split("T")[0];

        String[] parts = datePart.split("-");
        if (parts.length >= 3) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR,
                    Integer.parseInt(parts[0]));
            calendar.set(Calendar.MONTH,
                    Integer.parseInt(parts[1]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));

            long difference = Calendar.getInstance().getTime().getTime()
                    - calendar.getTime().getTime();
            long diffDays = difference / (24 * 60 * 60 * 1000);

            return daysPassed(diffDays);
        } else
            return "";
    }

    public static String daysPassed(long daysPassed) {

        String output = "";
        if (daysPassed == 0)
            output += "today";
        else if (daysPassed == 1)
            output += "yesterday";
        else if (daysPassed > 1)
            output += daysPassed + "d";
        return output;
    }

    public static String likes(int likes) {
        String output = "";
        if (likes == 0)
            output += "0 likes";
        else if (likes == 1)
            output += likes + " like";
        else if (likes > 1)
            output += likes + " likes";
        return output;
    }

    public static String likesInDays(int likes, String dateCreated) {
        String datePart = dateCreated.split(" ")[0];
        String[] parts = datePart.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,
                Integer.parseInt(parts[0]));
        calendar.set(Calendar.MONTH,
                Integer.parseInt(parts[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));

        long difference = Calendar.getInstance().getTime().getTime()
                - calendar.getTime().getTime();
        long diffDays = difference / (24 * 60 * 60 * 1000);

        String output = "";
        if (likes == 0)
            output += likes + " likes";
        else if (likes == 1)
            output += likes + " like";
        else if (likes > 1)
            output += likes + " likes";

        if (diffDays == 0)
            output += " first day";
        else if (diffDays == 1)
            output += " in 1 day";
        else if (diffDays > 1)
            output += " in " + diffDays + " days";

        return output;
    }

    public static String noOfTracks(int no) {
        String output = "";
        if (no == 0)
            output += "no track";
        else if (no == 1)
            output += "1 track";
        else if (no > 1)
            output += no + " tracks";
        return output;
    }

    public static String generateUniqueID() {
        return UUID.randomUUID().toString();
    }


    public static boolean isEmptyOrNull(String input) {
        return input == null || input.length() == 0;
    }

    public static String hashString(String secret) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(secret.getBytes());

            byte byteData[] = md.digest();

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSizeAsString(long size) {
        Double sizeDouble = (double) size;

        String unit = "B";
        if (sizeDouble > 1024) {
            sizeDouble = sizeDouble / 1024;
            unit = "KB";
        }
        if (sizeDouble > 1024) {
            sizeDouble = sizeDouble / 1024;
            unit = "MB";
        }

        return formatSize(sizeDouble) + " " + unit;
    }

    public static Double getSizeInMB(long size) {
        Double sizeDouble = (double) size;
        sizeDouble = sizeDouble / (1024 * 1024);

        return Double.valueOf(formatSize(sizeDouble));
    }

    public static String formatSize(double size) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(size);
    }

    public static String getFileMimeType(String fileName) {

        if (!fileName.isEmpty()) {
            if (fileName.endsWith(".png"))
                return "image/png";
            else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
                return "image/jpeg";
        }
        return null;
    }


    public static String generateClientApiKey() {
        return UUID.randomUUID().toString();
    }

    public static String generateTransactionCode() {
        String uniqueID = UUID.randomUUID().toString();
        String[] parts = uniqueID.split("-");
        String joinedParts = String.join("", parts);

        return joinedParts + System.currentTimeMillis();
    }

    public static String generateTransactionId() {
        String uniqueID = UUID.randomUUID().toString();
        String[] parts = uniqueID.split("-");
        String joinedParts = String.join("", parts);

        return joinedParts;
    }

    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }

    public static String generateReceiptNumber() {
        int fourDigitNumber = new Random().nextInt(9999);

        Date date = new Date();

        DateFormat dateFormatUTC = new SimpleDateFormat("yyMMdd", Locale.getDefault());
        String dateSequence = dateFormatUTC.format(date);

        dateFormatUTC = new SimpleDateFormat("HHmmss", Locale.getDefault());
        String timeSequence = dateFormatUTC.format(date);

        return /*String.format(Locale.getDefault(), "%05d", businessAId)
                + "-"
                +*/ String.format(Locale.getDefault(), "%04d", fourDigitNumber)
                + "-"
                + String.format(Locale.getDefault(), "%06d", Integer.parseInt(dateSequence))
                + "-"
                + String.format(Locale.getDefault(), "%06d", Integer.parseInt(timeSequence));
    }

    public static String generateOtp() {

        String fourDigitAlphabetic = Strings.toUpperCase(RandomStringUtils.randomAlphabetic(4));
        int fourDigitNumber = new Random().nextInt(9999);
        return fourDigitAlphabetic + "-"
                + String.format(Locale.getDefault(), "%04d", fourDigitNumber);
    }

    public static String gererateDefaultPassword() {
        return Strings.toUpperCase(RandomStringUtils.randomAlphanumeric(12));
    }

    public static String internationalizePhoneNumber(String countryCode, String phone) throws Exception {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(phone, countryCode);
        return phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL).replace(" ", "");
    }

    public static String formatAmount(Double amount) {
        return String.format("%,.2f", amount);
    }

    public static String getInvmSecureHash(String data, String secret) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(Hex.decodeHex(secret.toCharArray()), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)))).toUpperCase();
    }

    public static String generateBusinessOrBranchCode(String name) {
        name = name.trim();
        name = name.toLowerCase();
        name = name.replace(".", "");
        name = name.replaceAll("\\W", "");
        name = name.replaceAll("\\s{2,}", "-");
        name = name.replace("limited", "");
        name = name.replace("ltd", "");
        name = String.join("-", Arrays.asList(name.split(" ")));
        int fourDigitNumber = new Random().nextInt(999);
        String code =  name + "-" + String.format(Locale.getDefault(), "%03d", fourDigitNumber);
        return code.toLowerCase();
    }

    public static String getInitials(String name) {
        if (name.length() == 0)
            return name;
        StringBuilder str = new StringBuilder();

        String[] words = name.split(" ");
        for (String word : words) {
            if (word.trim().length() > 0){
                str.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        return str.toString().replaceAll("[^a-zA-Z0-9]", "");
    }

    public static <T> ApiResponse<T> wrapInApiResponse(T data, String resquestId) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(ResponseMessage.SUCCESS.getCode());
        response.setMessage(ResponseMessage.SUCCESS.toString());
        response.setRequestId(resquestId);
        response.setData(data);
        return response;
    }

    @SuppressWarnings("unchecked")
    public static <T, W> ApiResponse<PagedContent<W>> wrapInPagedApiResponse(Page page, List<W> data, String sessionId) {
        ApiResponse<PagedContent<W>> response = new ApiResponse<>();
        response.setCode(ResponseMessage.SUCCESS.getCode());
        response.setMessage(ResponseMessage.SUCCESS.toString());
        response.setRequestId(sessionId);
        response.setData(new PagedContent(page, data));
        return response;
    }

    public static HashMap<String, String> getDuplicateConstraintMessage(String cause) {
        int beginIndex = cause.lastIndexOf("Detail:");
        String msg = cause.substring(beginIndex);
        String[] msgHalves = msg.split("=");
        String firstHalf = msgHalves[0];
        String secondHalf = msgHalves[1];
        String key = firstHalf.substring(firstHalf.indexOf("(") + 1, firstHalf.indexOf(")"));
        if (key.contains("_")) {
            key = String.join(" ", key.split("_"));
        }

        String value = secondHalf.substring(secondHalf.indexOf("("), secondHalf.indexOf(")") + 1);
        HashMap<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("value", value);
        return map;
    }

    public static String getSQLConstraintMessage(String cause) {
        int beginIndex = cause.lastIndexOf("Detail:");
        String msg = cause.substring(beginIndex);
        String[] msgHalves = msg.split("=");
        String firstHalf = msgHalves[0];
        String key = firstHalf.substring(firstHalf.indexOf("(") + 1, firstHalf.indexOf(")"));
        String userMessage;
        if (key.contains(",")) {
            List<String> clean = new ArrayList<>();
            Arrays.stream(key.split(",")).forEach((s) -> {
                clean.add(String.join(" ", s.split("_")));
            });
            clean.set(clean.size() - 1, " and" + (String)clean.get(clean.size() - 1));
            userMessage = String.join(",", clean);
            userMessage = userMessage.replaceFirst(", and", " and");
        } else {
            String join = String.join(" ", key.split("_"));
            userMessage = join.substring(0, 1).toUpperCase() + join.substring(1);
        }

        return userMessage;
    }

}
