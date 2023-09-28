import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const RequestContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 5%;
  width: 100%;
  margin: 0 auto;
`

export const RequestHeader = styled.h2`
  display: block;
  width: 100%;
  text-align: center;
  padding-bottom: 10px;
  margin-bottom: 30px;
  font-size: 30px;
  border-bottom: 1px solid ${palette.divider};
`

export const RequestForm = styled.form`
  width: 100%;
  padding: 0 2%;
`

export const RequestFormList = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 30px;
  padding-left: 0;
`

export const RequestFormEl = styled.li`
  display: flex;
  gap: 10px;
  align-items: center;
  @media (max-width: 768px) {
    align-items: start;
    flex-direction: column;
  }
`

export const ElementTitle = styled.span`
  display: inline-block;
  line-height: 24px;
  height: 24px;
  font-size: 24px;
  font-weight: 600;
  width: 150px;
`

export const ElementInput = styled.input`
  width: 100%;
  height: 60px;
  border-radius: 10px;
  border: 1px solid ${palette.divider};
  box-sizing: border-box;
  padding: 0 20px;
  font-size: 20px;
  &:focus {
    outline: none;
  }
  ${breakpoints.large} {
    width: calc(100% - 150px);
    height: 72px;
    font-size: 24px;
  }
`

export const ElementSourceInput = styled.input`
  width: 100%;
  height: 60px;
  border-radius: 10px;
  border: 1px solid ${palette.divider};
  padding: 0 20px;
  font-size: 20px;
  margin-right: 0px;
  box-sizing: border-box;
  &:focus {
    outline: none;
  }
  ${breakpoints.large} {
    height: 72px;
    width: 25%;
    font-size: 24px;
    margin-bottom: 0px;
  }
`

export const LikeInputDiv = styled.div`
  display: flex;
  width: 100%;
  height: 60px;
  border-radius: 10px;
  border: 1px solid ${palette.divider};
  box-sizing: border-box;
  padding: 0 20px;
  font-size: 20px;
  gap: 10px;
  align-items: center;
  ${breakpoints.large} {
    width: calc(100% - 150px);
    height: 72px;
  }
`
export const InvisibleInput = styled.input`
  width: 100%;
  font-size: 20px;
  border: none;
  &:focus {
    outline: none;
  }
  ${breakpoints.large} {
    height: 99%;
    font-size: 24px;
  }
`

export const ElementSpan = styled.span`
  font-size: 24px;
`

export const PlusButton = styled.button`
  width: 60px;
  height: 60px;
  border: 1px solid ${palette.divider};
  border-radius: 10px;
  font-size: 24px;
  ${breakpoints.large} {
    height: 72px;
    width: 72px;
  }
`

export const ButtonList = styled.div`
  display: flex;
  justify-content: center;
  gap: 20px;
  width: auto;
  margin: 0 auto;
  margin: 50px 0;
  ${breakpoints.large} {
    margin-top: 300px;
  }
`

export const SearchButton = styled.button`
  width: 146px;
  height: 44px;
  text-align: center;
  color: ${palette.main};
  border: 1px solid ${palette.main};
  border-radius: 10px;
  ${breakpoints.large} {
    height: 72px;
    font-size: 20px;
  }
`

export const WritingButton = styled.button`
  width: 146px;
  height: 44px;
  line-height: 44px;
  text-align: center;
  background-color: ${palette.main};
  color: ${palette.white};
  border-radius: 5px;
  margin-bottom: 95px;
  ${breakpoints.large} {
    margin-bottom: 0px;
  }
`
