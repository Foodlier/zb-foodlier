import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/user/ProfilePage.styled'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'
import UserChefInfo from '../../components/user/UserChefInfo'
import UserRecipeItem from '../../components/user/UserRecipeItem'
import UserReviewedItem from '../../components/user/UserReviewedItem'
import axiosInstance from '../../utils/FetchCall'

interface User {
  memberId: number
  nickname: string
  receivedHeart: 0
  profileUrl: string
  isChef: boolean
  chefMemberId: number
}

const ProfilePage = () => {
  const { IcFavoriteFill, ChefHat, InitialUserImg } = useIcon()
  const [user, setUser] = useState<User>()
  const [isUserLoading, setIsUserLoading] = useState(false)
  const navigate = useNavigate()
  const params = useParams() // 파람에 있는 member Id 가져와야함
  const userId = params.id

  const getUserInfo = async () => {
    try {
      // 유저 기본 정보 가져오기
      const res = await axiosInstance.get(`/api/profile/public/${userId}`)
      console.log('기본 정보', res.data)
      setUser(res.data)
      setIsUserLoading(true)
    } catch (error) {
      console.log(error)
    }
  }

  const goToMore = (
    sort: string,
    nickname: string | undefined,
    id: number | undefined
  ) => {
    const Info = {
      sort,
      nickname,
      id,
    }
    navigate(`/profile/${userId}/more`, { state: Info })
  }

  useEffect(() => {
    getUserInfo()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return (
    <>
      <Header />
      <S.MyProfileContainer>
        <S.NormalInfoContainer>
          <S.UserContainer>
            <S.UserImgContainer>
              {user?.profileUrl ? (
                <S.UserImg src={user.profileUrl} alt="프로필 이미지" />
              ) : (
                <InitialUserImg size={6} />
              )}
              {user?.isChef && (
                <S.UserIsCooker>
                  <ChefHat size={1.2} />
                  요리사
                </S.UserIsCooker>
              )}
            </S.UserImgContainer>
            <S.UserNickname>{user?.nickname}</S.UserNickname>
          </S.UserContainer>
          <S.LikeDiv>
            <IcFavoriteFill size={2.5} color={palette.main} />
            {user?.receivedHeart}
          </S.LikeDiv>
        </S.NormalInfoContainer>

        {isUserLoading && user?.isChef && (
          <UserChefInfo
            EA={2}
            nickName={user?.nickname}
            chefMemberId={user?.chefMemberId}
            onlyReview={false}
            isRow={false}
          />
        )}

        <S.MyUploadContainer>
          <S.MyUploadIntro>
            <S.MyUploadTitle>{user?.nickname}님의 올린 게시물</S.MyUploadTitle>
            <S.UploadMoreBtn
              type="button"
              onClick={() => {
                goToMore('recipe', user?.nickname, user?.memberId)
              }}
            >
              +
            </S.UploadMoreBtn>
          </S.MyUploadIntro>
          {isUserLoading && <UserRecipeItem EA={4} memberId={user?.memberId} />}
        </S.MyUploadContainer>

        <S.MyReviewedContainer>
          <S.MyReviewedInto>
            <S.MyReviewedTitle>
              {user?.nickname}님이 받은 레시피 후기
            </S.MyReviewedTitle>
            <S.ReviewedMoreBtn
              type="button"
              onClick={() => {
                goToMore('recipeReview', user?.nickname, user?.memberId)
              }}
            >
              +
            </S.ReviewedMoreBtn>
          </S.MyReviewedInto>
          <S.ReviewedList>
            {isUserLoading && (
              <UserReviewedItem EA={2} memberId={user?.memberId} />
            )}
          </S.ReviewedList>
        </S.MyReviewedContainer>
      </S.MyProfileContainer>

      <S.SpacingDiv />

      <BottomNavigation />
    </>
  )
}

export default ProfilePage
