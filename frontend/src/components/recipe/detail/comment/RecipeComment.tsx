import React, { useState, useEffect, useRef } from 'react'
import { useParams } from 'react-router-dom'
import * as S from '../../../../styles/recipe/detail/comment/RecipeComment.styled'
import axiosInstance from '../../../../utils/FetchCall'
import CommonButton from '../../../ui/Button'

interface CommentItem {
  id: number
  message: string
  isDeleted: boolean
  nickname: string
  createdAt: string
  profileUrl: string
}
const RecipeComment = () => {
  // const [recipeId] = useState(10)
  const { recipeId } = useParams()
  const [commentList, setCommentList] = useState<CommentItem[]>([])
  const [isEditing] = useState(false)

  const [pageIdx] = useState(0)
  const pageSize = useRef(5)
  // const [pageSize] = useState(5)

  const [commentValue, setCommentValue] = useState('')

  // post
  const onSubmit = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()

    // const body = {
    //   message: commentValue,
    // }

    try {
      const res = await axiosInstance.post(`/comment/${recipeId}`, commentValue)

      if (res.status === 200) {
        console.log('작성한다', res.config.data)
        setCommentValue('')
      }
    } catch (error) {
      console.error('새 댓글 등록 중 오류 발생:', error)
    }
  }

  const handleClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCommentValue(event.currentTarget.value)
  }

  // get
  const getComment = async () => {
    // recipeId - 10 pageIdx - 0 pageSize - 5
    // recipeId는 useParam 쓰기
    const res = await axiosInstance.get(
      `/comment/${recipeId}/${pageIdx}/${pageSize.current}`
    )

    // pageIdx가 0 일때 pageSize가 5개씩 보여줌.
    // handleLoadMoreComments 버튼을 누르면 pageIdx가 1이고 pageSize가 10개
    //
    if (res.status === 200) {
      const commentData = res.data.content
      setCommentList(commentData)
    }
  }

  const handleLoadMoreComments = () => {}

  useEffect(() => {
    getComment()
  }, [])

  useEffect(() => {
    console.log(commentList)
  }, [commentList])

  return (
    <S.CommentContainer>
      <S.CommentTit>댓글</S.CommentTit>
      <S.CommentForm>
        <S.CommentInput
          type="text"
          placeholder="댓글을 입력하세요"
          onChange={handleClick}
          value={commentValue}
        />
        <S.CommentSubmit onClick={onSubmit}>등록</S.CommentSubmit>
      </S.CommentForm>
      <S.CommentList>
        {commentList.map((commentItem, idx) => (
          <div key={commentItem.id || idx}>
            <S.CommentItemWrapper>
              <S.CommentHeader>
                <S.CommentUserInfo>
                  <S.UserImg
                    src={commentItem.profileUrl}
                    alt={commentItem.nickname}
                  />
                  <S.UserNickname>{commentItem.nickname}</S.UserNickname>
                </S.CommentUserInfo>
                <S.CommentDate>{commentItem.createdAt}</S.CommentDate>
              </S.CommentHeader>
              {isEditing ? (
                <S.CommentEdit>
                  <S.CommentEditInput
                  // value={editValue}
                  // onChange={e => setEditValue(e.target.value)} // setEditValue를 사용하여 editValue를 업데이트
                  />
                  <S.CommentButtonWrap>
                    <CommonButton
                      size="small"
                      color="divider"
                      // onClick={handleConfirmEdit} // 함수를 호출하여 수정 내용을 서버에 업데이트
                    >
                      확인
                    </CommonButton>
                    <CommonButton
                      size="small"
                      color="divider"
                      // onClick={handleCancelEdit} // 함수를 호출하여 수정을 취소
                    >
                      취소
                    </CommonButton>
                  </S.CommentButtonWrap>
                </S.CommentEdit>
              ) : (
                <>
                  <S.CommentContent>{commentItem.message}</S.CommentContent>
                  <S.CommentButtonWrap>
                    <CommonButton
                      size="small"
                      color="divider"
                      // onClick={() => handleEditComment(commentItem.id)} // 함수를 호출하여 수정 모드로 전환
                    >
                      수정
                    </CommonButton>
                    <CommonButton
                      size="small"
                      color="divider"
                      // onClick={() => handleDeleteComment(commentItem.id)} // 함수를 호출하여 모달 오픈
                    >
                      삭제
                    </CommonButton>
                  </S.CommentButtonWrap>
                </>
              )}
            </S.CommentItemWrapper>
          </div>
        ))}
      </S.CommentList>
      <S.MoreButtonBox>
        <CommonButton
          onClick={handleLoadMoreComments}
          color="main"
          border="border"
        >
          댓글 더보기
        </CommonButton>
      </S.MoreButtonBox>
      {/* <Dialog
        title="댓글 삭제 확인"
        confirmText="확인"
        cancelText="취소"
        onConfirm={handleConfirmDelete} // 모달에서 삭제 확인
        onCancel={() => setIsDeleteModalOpen(false)} // 모달 close
        visible={isDeleteModalOpen}
      >
        댓글을 삭제하시겠습니까?
      </Dialog> */}
    </S.CommentContainer>
  )
}

export default RecipeComment
