def main() -> None:
    with open("testAll.txt", "wb") as file:
        barr = bytearray()
        
        for i in range(256):
            barr.extend([i]*(i+1))
        file.write(bytes(barr))
    with open("testHB.txt", "wb") as file:
        barr = bytearray()
        
        barr.extend([255]*(100))
        file.write(bytes(barr))
    with open("testABC3.txt", "w") as file:
        file.write("a"*10 + "b"*15 + "c"*30 + "d"*16 + "e"*29)

if __name__ == "__main__":
    main()