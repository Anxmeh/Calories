using Microsoft.EntityFrameworkCore.Migrations;

namespace CalorieCounter.Migrations
{
    public partial class AddUserSettings : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Weight",
                table: "UserProfile");

            migrationBuilder.CreateTable(
                name: "UserSettings",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false),
                    Age = table.Column<double>(type: "double precision", nullable: false),
                    Weight = table.Column<double>(type: "double precision", nullable: false),
                    Height = table.Column<double>(type: "double precision", nullable: false),
                    Chest = table.Column<double>(type: "double precision", nullable: false),
                    Waist = table.Column<double>(type: "double precision", nullable: false),
                    Hips = table.Column<double>(type: "double precision", nullable: false),
                    Hip = table.Column<double>(type: "double precision", nullable: false),
                    Shin = table.Column<double>(type: "double precision", nullable: false),
                    Wrist = table.Column<double>(type: "double precision", nullable: false),
                    Forearm = table.Column<double>(type: "double precision", nullable: false),
                    Neck = table.Column<double>(type: "double precision", nullable: false),
                    Calories = table.Column<double>(type: "double precision", nullable: false),
                    Bmi = table.Column<double>(type: "double precision", nullable: false),
                    FatPercentage = table.Column<double>(type: "double precision", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_UserSettings", x => x.Id);
                    table.ForeignKey(
                        name: "FK_UserSettings_AspNetUsers_Id",
                        column: x => x.Id,
                        principalTable: "AspNetUsers",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "UserSettings");

            migrationBuilder.AddColumn<double>(
                name: "Weight",
                table: "UserProfile",
                type: "double precision",
                nullable: false,
                defaultValue: 0.0);
        }
    }
}
