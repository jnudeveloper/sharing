package main

import (
    "context"
    "fmt"
    "log"
    "net/http"
    "os"
    "os/signal"
    "time"
    "net"
)


func GetOutboundIP() string {
    conn, err := net.Dial("udp", "8.8.8.8:80")
    if err != nil {
        log.Fatal(err)
    }
    defer conn.Close()
 
    localAddr := conn.LocalAddr().(*net.UDPAddr)
    fmt.Println(localAddr.String())
    return localAddr.IP.String()
}



/**
 * 说明：
 * 终端运行 go run service.go
 * 浏览器打开：http://localhost:3001/
 */

// service.go 是程序的启动入口，上一章说过里面是一个 Web 服务器
func main () {
    // 新建一个路由器实例 mux
    mux := http.NewServeMux();

    // 然后路由器添加一个首页的 Handle Func 函数，
    // 它接收一个字符串的 pattern 路径表达式和一个 handler 函数。handler 负责输出一行简单的文字
    mux.HandleFunc("/", func(writer http.ResponseWriter, request *http.Request) {
        log.Println("11")
        fmt.Fprintf(writer, "[0.0.3]Hey guys, my ip is " + GetOutboundIP())
    })

    timeOut := time.Second * 45

    srv := &http.Server{
        Addr: ":3001",
        Handler: mux,
        ReadTimeout: timeOut, // 读超时时间
        WriteTimeout: timeOut, // 写 超时时间
        IdleTimeout: timeOut * 2, // 空闲 超时时间
        MaxHeaderBytes: 1 << 20, // 头部数据大小
    }

    go func() {
        if err := srv.ListenAndServe(); err != nil {
            log.Fatalf(" listen and serve http server fail:\n %v ", err)
        }
        log.Println("server started")
    }()

    exit := make(chan os.Signal)
    signal.Notify(exit, os.Interrupt)
    <-exit
    ctx, cacel := context.WithTimeout(context.Background(), timeOut)
    defer cacel()
    err := srv.Shutdown(ctx)
    log.Println("shutting down now. ", err)
    os.Exit(0)

    // http.ListenAndServe(":3001", mux)
}
