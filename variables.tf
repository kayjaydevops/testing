variable "ami_id" {
    description ="AMI ID to launch EC2"
    type =string
}

variable "instance_type" {
    description = "Instance type for EC2"
    type =string
    default ="t2.micro"
}