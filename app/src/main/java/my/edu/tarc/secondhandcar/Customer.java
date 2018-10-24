package my.edu.tarc.secondhandcar;

/**
 * Created by Bbao on 25/10/2018.
 */

public class Customer {
    private String custID,custName,custContactNo,custEmail,password;

    public Customer(){

    }

    public Customer(String custID,String custName,String custContactNo,String custEmail,String password){
        this.custID=custID;
        this.custContactNo=custContactNo;
        this.custEmail=custEmail;
        this.custName=custName;
        this.password=password;
    }
    public Customer(String email,String password){
        this.custEmail=email;
        this.password=password;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setCustContactNo(String custContactNo) {
        this.custContactNo = custContactNo;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustID() {
        return custID;
    }

    public String getCustName() {
        return custName;
    }

    public String getCustContactNo() {
        return custContactNo;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custID='CU" + custID + '\'' +
                ", custName='" + custName + '\'' +
                ", custEmail='" + custEmail + '\'' +
                ", custContactNo=" + custContactNo +
                ", password='" + password + '\'' +
                '}';
    }
}
