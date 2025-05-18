         
# Trò Chơi Caro - Ứng Dụng JavaFX

## Giới thiệu

Trò Chơi Caro là một ứng dụng được phát triển bằng JavaFX, cho phép người chơi tham gia vào trò chơi cờ caro truyền thống trên máy tính. Ứng dụng cung cấp giao diện người dùng thân thiện, hiệu ứng âm thanh và khả năng chơi với máy tính.

## Tính năng

- **Đăng nhập/Đăng ký**: Hệ thống tài khoản người dùng để lưu trữ thông tin và lịch sử chơi
- **Giao diện trực quan**: Thiết kế giao diện đơn giản, dễ sử dụng với JavaFX
- **Hiệu ứng âm thanh**: Âm thanh khi đánh quân, thắng, thua và hòa
- **Chơi với máy tính**: Tích hợp AI cho phép người chơi đấu với máy tính
- **Lưu trữ dữ liệu**: Sử dụng SQL Server để lưu trữ thông tin người chơi và lịch sử trận đấu

## Yêu cầu hệ thống

- Java Development Kit (JDK) 21 trở lên
- JavaFX 21.0.7
- SQL Server (MSSQL)


## Cài đặt

1. Cài đặt JDK 21 hoặc cao hơn
2. Cài đặt SQL Server và tạo cơ sở dữ liệu

## Hướng dẫn sử dụng

1. **Đăng ký tài khoản**: Mở ứng dụng và chọn "Đăng ký" để tạo tài khoản mới
2. **Đăng nhập**: Sử dụng tài khoản đã đăng ký để đăng nhập vào hệ thống
3. **Bắt đầu trò chơi**: Sau khi đăng nhập, chọn chế độ chơi (với máy tính hoặc người chơi khác)
4. **Chơi game**: Nhấp vào các ô trên bàn cờ để đặt quân X hoặc O
5. **Kết thúc trò chơi**: Trò chơi kết thúc khi một người chơi tạo được 5 quân liên tiếp theo hàng ngang, dọc hoặc chéo

## Cấu trúc dự án

- **src/controller/**: Chứa các lớp điều khiển cho giao diện người dùng
- **src/model/**: Chứa các lớp mô hình dữ liệu
- **src/view/**: Chứa các file FXML và CSS cho giao diện người dùng
- **src/utils/**: Chứa các tiện ích như SoundManager để quản lý âm thanh
- **src/main/**: Chứa lớp Main để khởi động ứng dụng

## Công nghệ sử dụng

- **JavaFX**: Framework để xây dựng giao diện người dùng
- **FXML**: Ngôn ngữ đánh dấu để định nghĩa giao diện người dùng
- **CSS**: Định dạng và tạo kiểu cho giao diện
- **JDBC**: Kết nối và tương tác với cơ sở dữ liệu SQL Server
- **Java Sound API**: Phát âm thanh trong trò chơi
