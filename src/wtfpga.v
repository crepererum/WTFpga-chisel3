`timescale 1ns / 1ps
//define our module and it's inputs/outputs
module top(
	input CLK,
	input BTN1,
	input BTN2,
	input BTN3,
	input BTN_N,
	input [7:0] sw,
	output [4:0] led,
	output [6:0] seg,
	output ca
    );

//define wires and registers here
	wire [7:0] disp0,disp1;
	wire displayClock;
	// wire wire1, wire2;
    reg [7:0] storedValue;
    wire [7:0] dispValue;
    wire [7:0] sum;
    wire [7:0] div;

//parallel assignments can go here
	// assign disp0 = 0;
	// assign disp1 = 0;
	// assign led[3:1]=5'b0;
	// assign led[0]=wire1;
	// assign led[4]=wire2;
	// assign wire1=BTN1;
	// assign wire2=BTN3;
    assign led[4:0] = sw[4:0];
    assign dispValue = BTN3?
        div:
        (
            BTN1?
            sum:
            (
                BTN2?
                storedValue:
                sw
            )
        );
    assign sum = sw + storedValue;
    assign div = sw - storedValue;

//always @ blocks can go here
	always @(negedge BTN_N) begin
		storedValue <= sw;
	end

//instantiate modules here
	nibble_to_seven_seg nibble0(
		.nibblein(dispValue[0:3]),
		.segout(disp0)
	);
	nibble_to_seven_seg nibble1(
		.nibblein(dispValue[7:4]),
		.segout(disp1)
	);

	clkdiv displayClockGen(
		.clk(CLK),
		.clkout(displayClock)
	);

	seven_seg_mux display(
		.clk(displayClock),
		.disp0(disp0),
		.disp1(disp1),
		.segout(seg),
		.disp_sel(ca)
	);

endmodule
